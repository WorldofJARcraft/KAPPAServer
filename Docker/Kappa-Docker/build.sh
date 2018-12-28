#!/usr/bin/env bash
cd ../..
mvn clean package
cp ./target/*.jar ./Docker/Kappa-Docker/kappa.jar
docker build -t kappa ./kappa.Dockerfile