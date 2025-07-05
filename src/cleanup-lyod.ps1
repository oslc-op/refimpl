<#
.SYNOPSIS
    Deletes files in a directory that were not modified in the last X minutes and contain the phrase "Start of user code".

.PARAMETER DirectoryPath
    The path to the directory to search for files.

.PARAMETER Minutes
    The number of minutes to determine if a file is considered old.

.EXAMPLE
    .\cleanup-lyod.ps1 -DirectoryPath "server-rm" -Minutes 30
#>

param (
    [Parameter(Mandatory=$true)]
    [string]$DirectoryPath,

    [Parameter(Mandatory=$true)]
    [int]$Minutes
)

# Calculate the cutoff time
$cutoffTime = (Get-Date).AddMinutes(-$Minutes)

# Get all files in the directory and its subdirectories
$files = Get-ChildItem -Path $DirectoryPath -File -Recurse

foreach ($file in $files) {
    # Check if the file was modified before the cutoff time
    if ($file.LastWriteTime -lt $cutoffTime) {
        try {
            # Read the file content
            $content = Get-Content -Path $file.FullName -Raw -ErrorAction Stop

            # Check if the file content contains the phrase "Start of user code"
            if ($content -like "*Start of user code*") {
                # Delete the file
                Remove-Item -Path $file.FullName -Force
                Write-Host "Deleted file: $($file.FullName)"
            }
        } catch {
            Write-Host "Error processing file: $($file.FullName). $_" -ForegroundColor Red
        }
    }
}

Write-Host "File deletion process completed."