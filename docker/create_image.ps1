#!/bin/bash
docker login
docker build -t hugost010/webapp07 -f ./docker/Dockerfile .
docker push hugost010/webapp07
