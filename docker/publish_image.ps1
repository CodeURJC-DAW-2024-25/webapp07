# Variables
$dockerHubUser = "hugost010"  # <-- CAMBIA esto a tu usuario real
$image = "$dockerHubUser/volteretacroqueta2:latest"

# Pushear imágenes a Docker Hub
docker push $image
