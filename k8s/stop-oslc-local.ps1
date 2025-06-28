# Stop and clean up all OSLC local development resources
# Stops port-forwarding, deletes k8s resources, and removes Traefik config

$ErrorActionPreference = 'SilentlyContinue'

# 1. Stop all kubectl port-forward processes started for OSLC services
Write-Host "Stopping kubectl port-forward processes for OSLC services..."
Get-WmiObject Win32_Process | Where-Object {
    $_.CommandLine -match 'kubectl port-forward deployment/server-(am|cm|qm|rm)-deployment (880[0-3]):8080'
} | ForEach-Object {
    Write-Host "Stopping process $($_.ProcessId): $($_.CommandLine)"
    Stop-Process -Id $_.ProcessId -Force
}
Write-Host "All kubectl port-forward processes for OSLC services have been stopped."

# 2. Delete all resources created by the local-ports overlay
Write-Host "Deleting all OSLC deployments, services, and ingresses (local-ports overlay)..."
kubectl delete -k refimpl/k8s/overlays/local-ports --ignore-not-found

# 3. Remove Traefik HelmChartConfig if it was applied
if (kubectl -n kube-system get helmchartconfig traefik -o name 2>$null) {
    Write-Host "Removing Traefik HelmChartConfig..."
    kubectl -n kube-system delete helmchartconfig traefik
}

Write-Host "Kubernetes environment restored to pre-local-ports state."
