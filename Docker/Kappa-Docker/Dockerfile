FROM openjdk:10-jre-slim
RUN groupadd -g 1000 -r kappa && useradd -d /home/kappa -g kappa -p KappaPW -u 1000 -m -s /bin/bash kappa
COPY ./kappa.jar /home/kappa/kappa.jar
USER kappa
ENTRYPOINT ["java", "-jar", "/home/kappa/kappa.jar"]
