<#
.SYNOPSIS
    Lists obsoletions, deprecations, and outdated dependencies for a Maven project.

.DESCRIPTION
    This script runs various Maven plugins to gather information about:
    1. Code deprecations (calls to @Deprecated methods/classes).
    2. Outdated dependencies (using versions-maven-plugin).
    3. Outdated plugins (using versions-maven-plugin).
    4. Unused declared dependencies (using maven-dependency-plugin).

.PARAMETER IncludeTransitive
    If set, includes transitive dependencies in the outdated dependencies report.

.PARAMETER OnlyDeprecations
    If set, only checks for code deprecations and skips dependency/plugin checks.

.PARAMETER PomFile
    Path to the pom.xml file. Defaults to "pom.xml" in the script's directory.
#>
param(
    [switch]$IncludeTransitive,
    [switch]$OnlyDeprecations,
    [string]$PomFile
)

$ErrorActionPreference = "Continue"

if ($PomFile) {
    $pomPath = Resolve-Path $PomFile
} else {
    $pomPath = Join-Path $PSScriptRoot "pom.xml"
}

# Check Java version mismatch
try {
    $javaExe = (Get-Command java -ErrorAction Stop).Source
    # Resolve symlinks if possible or just take the path
    $javaItem = Get-Item $javaExe
    if ($javaItem.LinkType -eq "SymbolicLink") {
        $javaExe = $javaItem.Target
    }
    $javaExePath = Split-Path (Split-Path $javaExe)
    
    if ($env:JAVA_HOME) {
        $envJavaHome = (Get-Item $env:JAVA_HOME).FullName.TrimEnd('\/')
        $exeJavaHome = (Get-Item $javaExePath).FullName.TrimEnd('\/')
        
        if ($envJavaHome -ne $exeJavaHome) {
             Write-Warning "JAVA_HOME ($env:JAVA_HOME) differs from 'java' in PATH ($javaExePath). Maven might use an unexpected version."
        }
    }
} catch {
    # Ignore
}

function Get-MavenOutput {
    param(
        [string[]]$Goals,
        [string[]]$Params
    )
    $mvnCmd = "mvn"
    if ($IsWindows) { $mvnCmd = "mvn.cmd" }
    
    # -B for batch mode (less noise in logs)
    $cmdArgs = @("-B", "-f", $pomPath) + $Goals + $Params
    
    # Capture all output (stdout and stderr)
    $output = & $mvnCmd $cmdArgs 2>&1
    return $output
}

function Parse-VersionsOutput {
    param(
        [string[]]$OutputLines,
        [switch]$IncludeTransitive
    )
    
    $results = @()
    $bufferedArtifact = ""
    $currentSection = "None"

    foreach ($line in $OutputLines) {
        # Detect section headers
        if ($line -match "The following dependencies in Dependencies have newer versions:") {
            $currentSection = "Direct"
            $bufferedArtifact = ""
            continue
        }
        if ($line -match "The following dependencies in Dependency Management have newer versions:") {
            $currentSection = "Transitive"
            $bufferedArtifact = ""
            continue
        }
        if ($line -match "The following plugin updates are available:") {
            $currentSection = "Direct" # Plugins are treated as direct/always show
            $bufferedArtifact = ""
            continue
        }

        # Determine if we should capture this section
        $capture = $false
        if ($currentSection -eq "Direct") { $capture = $true }
        if ($currentSection -eq "Transitive" -and $IncludeTransitive) { $capture = $true }
        
        if (-not $capture) {
            continue
        }

        $cleanLine = $line -replace "^\[INFO\]\s*", ""
        
        # Check for update line (contains "->")
        if ($cleanLine -match "\s->\s") {
            # Check if this line also contains the artifact name (group:artifact)
            # Regex: start of line, non-spaces, colon, non-spaces
            if ($cleanLine -match "^[^: ]+:[^: ]+") {
                # Full line
                $results += $cleanLine
                $bufferedArtifact = ""
            } elseif ($bufferedArtifact) {
                # Continuation line
                # Remove trailing dots from buffer
                $prefix = $bufferedArtifact -replace "\.+$", ""
                # Remove trailing spaces from prefix
                $prefix = $prefix.Trim()
                $results += "$prefix " + $cleanLine.Trim()
                $bufferedArtifact = ""
            } else {
                # Orphan update line (shouldn't happen if logic is correct, but just in case)
                $results += $cleanLine
            }
        }
        # Check for artifact line (group:artifact, no "->")
        # We look for lines starting with group:artifact
        elseif ($cleanLine -match "^(?<name>[^: ]+:[^: ]+)") {
            $bufferedArtifact = $cleanLine
        }
    }
    return $results | Select-Object -Unique
}

Write-Host "Gathering information... (this may take a minute)" -ForegroundColor Gray

# 1. Code Deprecations
Write-Host "`n1. Code Deprecations (Calls to @Deprecated)" -ForegroundColor Cyan
Write-Host "------------------------------------------" -ForegroundColor DarkCyan
# We need clean compile to ensure we see warnings for all files
$compileOut = Get-MavenOutput -Goals @("clean", "compile") -Params @("-Dmaven.compiler.showDeprecation=true", "-Dmaven.compiler.showWarnings=true")
# Check for build failure
if ($compileOut -match "BUILD FAILURE") {
    Write-Host "Build failed! Some results may be missing." -ForegroundColor Red
}

# Filter for [deprecation] tag or "has been deprecated" message
$deprecations = $compileOut | Where-Object { $_ -match "\[deprecation\]" -or $_ -match "has been deprecated" } | Select-Object -Unique

if ($deprecations) {
    foreach ($line in $deprecations) {
        # Format: [WARNING] /path/to/file.java:[12,34] [deprecation] ...
        # Strip [WARNING] for cleaner output
        Write-Host ($line -replace "^\[WARNING\]\s*", "")
    }
} else {
    Write-Host "None found." -ForegroundColor Green
}

if (-not $OnlyDeprecations) {
    # 2. Outdated Dependencies
    $depTitle = "2. Outdated Dependencies (Direct)"
    if ($IncludeTransitive) { $depTitle = "2. Outdated Dependencies (Direct & Transitive)" }
    Write-Host "`n$depTitle" -ForegroundColor Cyan
    Write-Host "------------------------" -ForegroundColor DarkCyan
    $verDepsOut = Get-MavenOutput -Goals @("versions:display-dependency-updates")
    
    if ($verDepsOut -match "BUILD FAILURE") {
        Write-Host "Build failed! Some results may be missing." -ForegroundColor Red
    }

    $outdatedDeps = Parse-VersionsOutput -OutputLines $verDepsOut -IncludeTransitive $IncludeTransitive

    if ($outdatedDeps) {
        foreach ($line in $outdatedDeps) {
            Write-Host $line
        }
    } else {
        Write-Host "None found." -ForegroundColor Green
    }

    # 3. Outdated Plugins
    Write-Host "`n3. Outdated Plugins" -ForegroundColor Cyan
    Write-Host "-------------------" -ForegroundColor DarkCyan
    $verPluginsOut = Get-MavenOutput -Goals @("versions:display-plugin-updates")
    
    if ($verPluginsOut -match "BUILD FAILURE") {
        Write-Host "Build failed! Some results may be missing." -ForegroundColor Red
    }

    $outdatedPlugins = Parse-VersionsOutput -OutputLines $verPluginsOut -IncludeTransitive $true # Always show plugins

    if ($outdatedPlugins) {
        foreach ($line in $outdatedPlugins) {
            Write-Host $line
        }
    } else {
        Write-Host "None found." -ForegroundColor Green
    }

    # 4. Unused Dependencies
    Write-Host "`n4. Unused Declared Dependencies" -ForegroundColor Cyan
    Write-Host "-------------------------------" -ForegroundColor DarkCyan
    $analyzeOut = Get-MavenOutput -Goals @("dependency:analyze") -Params @("-DignoreNonCompile=true")

    if ($analyzeOut -match "BUILD FAILURE") {
        Write-Host "Build failed! Some results may be missing." -ForegroundColor Red
    }

    $capturing = $false
    $unusedDeps = @()
    foreach ($line in $analyzeOut) {
        if ($line -match "Unused declared dependencies found") {
            $capturing = $true
            continue
        }
        
        if ($capturing) {
            # Stop capturing if we hit another section or INFO log that isn't an artifact
            # Artifact lines in this section usually start with [WARNING] and contain colons like group:artifact:ver
            if ($line -match "^\[WARNING\]\s+.*:.*:.*") {
                $unusedDeps += ($line -replace "^\[WARNING\]\s*", "")
            } elseif ($line -match "^\[INFO\]" -or ($line -match "^\[WARNING\]" -and $line -notmatch "dependencies found")) {
                # If we hit a new warning header or info, stop
                $capturing = $false
            }
        }
    }

    $unusedDeps = $unusedDeps | Select-Object -Unique
    if ($unusedDeps) {
        foreach ($item in $unusedDeps) {
            Write-Host $item.Trim()
        }
    } else {
        Write-Host "None found." -ForegroundColor Green
    }
}

