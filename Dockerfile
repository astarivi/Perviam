FROM bellsoft/liberica-openjre-alpine:21.0.4-9-cds AS builder

RUN mkdir /opt/build
WORKDIR /opt/build
COPY perviam .
RUN gradlew shadowJar

FROM bellsoft/liberica-openjre-alpine:21.0.4-9-cds

COPY --from=builder /opt/build/build/perviam.jar /root/perviam.jar
CMD ["java", "-jar", "perviam.jar"]