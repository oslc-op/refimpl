# Build, deploy, and port-forward all OSLC reference implementation services for Rancher Desktop (Moby)
# Run this script from the root of your repository (c:\src\oslc)

# $ErrorActionPreference = 'Stop'

# Build images
Write-Host "Building server-am..."
docker build -t localhost/server-am:latest -f refimpl/src/server-am/Dockerfile refimpl/src

Write-Host "Building server-cm..."
docker build -t localhost/server-cm:latest -f refimpl/src/server-cm/Dockerfile refimpl/src

Write-Host "Building server-qm..."
docker build -t localhost/server-qm:latest -f refimpl/src/server-qm/Dockerfile refimpl/src

Write-Host "Building server-rm..."
docker build -t localhost/server-rm:latest -f refimpl/src/server-rm/Dockerfile refimpl/src

# Confirm images
Write-Host "\nBuilt images:"
docker images | findstr localhost/server-

# Deploy using Kustomize local-ports overlay
Write-Host "\nApplying local-ports overlay with Kustomize..."
kubectl apply -k refimpl/k8s/overlays/local-ports

# Wait for all pods to be ready
Write-Host "\nWaiting for all pods to be ready..."
kubectl wait --for=condition=Ready pod -l app=server-am --timeout=180s
kubectl wait --for=condition=Ready pod -l app=server-cm --timeout=180s
kubectl wait --for=condition=Ready pod -l app=server-qm --timeout=180s
kubectl wait --for=condition=Ready pod -l app=server-rm --timeout=180s

# Start port-forwarding for all deployments in background
Write-Host "\nStarting kubectl port-forward for all services..."
$pfJobs = @()
$pfJobs += Start-Process powershell -ArgumentList 'kubectl port-forward deployment/server-rm-deployment 8800:8080' -WindowStyle Hidden -PassThru
$pfJobs += Start-Process powershell -ArgumentList 'kubectl port-forward deployment/server-cm-deployment 8801:8080' -WindowStyle Hidden -PassThru
$pfJobs += Start-Process powershell -ArgumentList 'kubectl port-forward deployment/server-qm-deployment 8802:8080' -WindowStyle Hidden -PassThru
$pfJobs += Start-Process powershell -ArgumentList 'kubectl port-forward deployment/server-am-deployment 8803:8080' -WindowStyle Hidden -PassThru

# Wait longer for port-forwarding to establish
Write-Host "\nWaiting 15 seconds for port-forwarding to establish..."
Start-Sleep -Seconds 15

# Function to retry curl up to 3 times
function Test-Curl {
    param([string]$url)
    $tries = 0
    while ($tries -lt 10) {
        $tries++
        Write-Host "Attempt ${tries}: curl $url"
        $result = curl $url -v 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host $result
            return $true
        } else {
            Write-Host "Failed. Retrying in 3 seconds..."
            Start-Sleep -Seconds 5
        }
    }
    Write-Host "Failed to connect to $url after 3 attempts."
    return $false
}

# Verify endpoints with curl and retry
Write-Host "\nVerifying endpoints with curl..."
Write-Host "\nserver-rm (port 8800):"
Test-Curl "http://localhost:8800/"
Write-Host "\nserver-cm (port 8801):"
Test-Curl "http://localhost:8801/"
Write-Host "\nserver-qm (port 8802):"
Test-Curl "http://localhost:8802/"
Write-Host "\nserver-am (port 8803):"
Test-Curl "http://localhost:8803/"

Write-Host "\nAll images built, deployments applied, port-forwarding started, and endpoints checked."
