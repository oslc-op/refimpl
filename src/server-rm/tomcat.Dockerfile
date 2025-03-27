FROM docker.io/library/maven:3-eclipse-temurin-24 AS build

COPY . /src
WORKDIR /src
RUN mvn -B --no-transfer-progress -DskipTests clean package -pl server-rm -am -Pwith-jstl-impl

FROM docker.io/library/tomcat:10-jre17

# do not write log files, log everything to the Docker daemon
COPY --from=build /src/server-rm/config/tomcat-log.properties $CATALINA_BASE/conf/logging.properties
ENV CATALINA_OUT=/dev/null

COPY --from=build /src/server-rm/target/*.war /usr/local/tomcat/webapps/ROOT.war
