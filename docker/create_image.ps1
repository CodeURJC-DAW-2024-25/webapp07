#!/bin/bash
docker login
docker build -t nicohht/volteretacroqueta -f ./docker/Dockerfile .
docker push nicohht/volteretacroqueta
