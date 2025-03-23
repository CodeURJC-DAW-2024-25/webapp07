#!/bin/bash
docker login
docker build -t hugost010/webapp07 -f ./Dockerfile .
docker push hugost010/webapp07
