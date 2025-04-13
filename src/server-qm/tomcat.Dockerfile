FROM docker.io/library/maven:3-eclipse-temurin-17 AS build

COPY . /src
WORKDIR /src
RUN mvn -B --no-transfer-progress -DskipTests clean package -pl server-qm -am -Pwith-jstl-impl

FROM docker.io/library/tomcat:11-jre17

# do not write log files, log everything to the Docker daemon
COPY --from=build /src/server-qm/config/tomcat-log.properties $CATALINA_BASE/conf/logging.properties
ENV CATALINA_OUT=/dev/null

COPY --from=build /src/server-qm/target/*.war /usr/local/tomcat/webapps/ROOT.war
