# Variables
$dockerHubUser = "hugost010"  # <-- CAMBIA esto a tu usuario real
$backendImage = "$dockerHubUser/volteretacroqueta-backend:latest"
$frontendImage = "$dockerHubUser/volteretacroqueta-frontend:latest"

# Pushear imágenes a Docker Hub
docker push $backendImage
docker push $frontendImage
