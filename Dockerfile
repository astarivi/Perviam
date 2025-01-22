FROM bellsoft/liberica-openjre-alpine:21.0.4-9-cds

RUN mkdir /root/storage /root/logs
ENV STORAGE_FOLDER='/root/storage'
EXPOSE 8080

WORKDIR /root
COPY ./perviam/build/libs/perviam.jar /root/perviam.jar
CMD ["java", "-jar", "perviam.jar"]