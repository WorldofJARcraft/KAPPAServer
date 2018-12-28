#!/usr/bin/env bash
cd ../..
cp ./src/main/resources/application.properties ./src/main/resources/application.properties.old
cp ./src/main/resources/docker.properties ./src/main/resources/application.properties
mvn clean package
mv ./src/main/resources/application.properties.old ./src/main/resources/application.properties
cp ./target/*.jar ./Docker/Kappa-Docker/kappa.jar
docker build -t kappa ./kappa.Dockerfile