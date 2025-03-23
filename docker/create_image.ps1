#!/bin/bash
docker login
docker build -t hugost010/volteretacroqueta -f ./docker/Dockerfile .
docker push hugost010/volteretacroqueta
