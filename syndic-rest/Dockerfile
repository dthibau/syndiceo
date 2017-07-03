FROM openjdk:8

MAINTAINER David THIBAU <david.thibau@gmail.com>

ADD ./target/syndic-rest.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/syndic-rest.jar", "--spring.profiles.active=docker,swagger,integration"]

EXPOSE 8080
