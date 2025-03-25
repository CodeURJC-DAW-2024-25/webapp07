#!/bin/bash
docker login
docker build -t nicohht/volteretacroqueta -f ./docker/Dockerfile .
