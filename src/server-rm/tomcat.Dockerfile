FROM docker.io/library/maven:3-eclipse-temurin-25 AS build

COPY . /src
WORKDIR /src

RUN mvn -B --no-transfer-progress \
    -DskipTests \
    clean package \
    -pl server-rm \
    -am \
    -Pwith-jstl-impl


FROM docker.io/library/tomcat:10.1-jre25-temurin

# Enable virtual threads for requests handled by the default HTTP connector.
RUN sed -i \
      's|<Connector port="8080" protocol="HTTP/1.1"|<Connector port="8080" protocol="HTTP/1.1" useVirtualThreads="true"|' \
      "$CATALINA_BASE/conf/server.xml" && \
    grep -q 'useVirtualThreads="true"' "$CATALINA_BASE/conf/server.xml"

# Do not write catalina.out; log through the container runtime.
COPY --from=build \
    /src/server-rm/config/tomcat-log.properties \
    "$CATALINA_BASE/conf/logging.properties"

ENV CATALINA_OUT=/dev/null

COPY --from=build \
    /src/server-rm/target/*.war \
    "$CATALINA_BASE/webapps/ROOT.war"

EXPOSE 8080
