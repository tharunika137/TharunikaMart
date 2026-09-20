FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:9-jdk17-temurin
RUN mkdir -p /usr/local/tomcat/data
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /build/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
