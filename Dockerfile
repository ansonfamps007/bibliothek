FROM openjdk:8-jdk-alpine
RUN mkdir -p /usr/app/build/repos/bibliothek-api/build
COPY build /usr/app/build/repos/bibliothek-api/build
WORKDIR /usr/app/build/repos/bibliothek-api
ENTRYPOINT ["java","-jar","build/libs/bibliothek-0.0.1-SNAPSHOT.jar"]