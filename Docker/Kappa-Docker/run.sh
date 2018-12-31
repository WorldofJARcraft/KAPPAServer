#!/usr/bin/env bash
docker rm -f kappa
docker run -p 8080:8080 --name kappa --restart=always --net=host -d kappa
