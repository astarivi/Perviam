ARG BASE_IMAGE=docker.io/eclipse-temurin:21-jre-alpine
FROM ${BASE_IMAGE}

RUN mkdir /root/storage /root/logs
ENV STORAGE_FOLDER='/root/storage'
EXPOSE 8080

WORKDIR /root
COPY ./perviam/build/libs/perviam.jar /root/perviam.jar
CMD ["java", "-jar", "perviam.jar"]