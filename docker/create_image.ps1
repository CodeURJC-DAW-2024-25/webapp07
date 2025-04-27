<#
.SYNOPSIS
    Construye la imagen Docker con frontend Angular y backend Java.

.DESCRIPTION
    Ejecuta docker build sobre el Dockerfile multi-stage que contiene
    la compilación de Angular y Maven, y etiqueta la imagen.
#>

param(
    [string]$ImageName = "hugost010/volteretacroqueta:latest"
)

# Ruta al Dockerfile (este script se asume dentro de docker/)
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$Dockerfile = Join-Path $ScriptDir "Dockerfile"
$Context    = Join-Path $ScriptDir ".."

Write-Host "==> Construyendo la imagen $ImageName ..." -ForegroundColor Cyan
docker build `
  --file $Dockerfile `
  --tag  $ImageName `
  $Context

if ($LASTEXITCODE -ne 0) {
    Write-Error "❌ Falló la construcción de la imagen."
    exit 1
}

Write-Host "✅ Imagen construida correctamente: $ImageName" -ForegroundColor Green