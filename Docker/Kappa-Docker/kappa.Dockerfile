FROM openjdk:8-jdk-alpine
RUN groupadd -g 1000 kappa && useradd -d /home/kappa -g kappa -p KappaPW -u 1000 kappa
COPY ./kappa.jar /home/kappa/kappa.jar
USER kappa
ENTRYPOINT ["java", "-jar", "/home/kappa/kappa.jar"]