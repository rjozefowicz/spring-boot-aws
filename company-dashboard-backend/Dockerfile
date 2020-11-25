FROM openjdk:12-jdk-alpine
EXPOSE 8080
COPY target/company-dashboard-backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]