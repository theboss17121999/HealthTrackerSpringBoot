FROM openjdk:22-jdk

ADD target/health-tracker-bose-server.jar healthtrackerbose.jar

ENTRYPOINT ["java", "-jar", "/healthtrackerbose.jar"]

LABEL authors="Shashwat Bose"