#!/usr/bin/env bash
cd ../..
cp ./src/main/resources/application.properties ./src/main/resources/application.properties.old
cp ./src/main/resources/docker.properties ./src/main/resources/application.properties
mvn -Dmaven.test.skip=true clean package
mv ./src/main/resources/application.properties.old ./src/main/resources/application.properties
cp ./target/kappa_server*.jar ./Docker/Kappa-Docker/kappa.jar
cd Docker/Kappa-Docker
sudo docker build -t kappa .
