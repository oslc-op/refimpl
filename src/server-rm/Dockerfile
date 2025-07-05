FROM docker.io/library/maven:3-eclipse-temurin-21-alpine AS build

WORKDIR /src

# Copy all pom.xml files and create module directories for Maven structure
COPY pom.xml .
COPY lib-common/pom.xml lib-common/pom.xml
COPY server-rm/pom.xml server-rm/pom.xml
COPY server-cm/pom.xml server-cm/pom.xml
COPY server-qm/pom.xml server-qm/pom.xml
COPY server-am/pom.xml server-am/pom.xml
COPY client-toolchain/pom.xml client-toolchain/pom.xml

# Copy source code for dependencies
COPY lib-common/ lib-common/
COPY server-rm/ server-rm/

# Build only the specific module and its dependencies
RUN mvn -B --no-transfer-progress -DskipTests clean package -pl server-rm -am

FROM docker.io/library/jetty:12-jre21-eclipse-temurin

# Add metadata
LABEL org.opencontainers.image.title="OSLC RefImpl RM Server"
LABEL org.opencontainers.image.description="OSLC Requirements Management Reference Implementation"
LABEL org.opencontainers.image.source="https://github.com/oslc-op/refimpl"
LABEL org.opencontainers.image.vendor="OSLC Open Project"
LABEL org.opencontainers.image.licenses="EPL-2.0"

# WARNING DO NOT CHANGE WORKDIR or set it back to what it was before
# $JETTY_BASE must be correct before starting Jetty

COPY --from=build /src/server-rm/target/*.war /var/lib/jetty/webapps/ROOT.war

RUN java -jar "$JETTY_HOME/start.jar" --add-modules=ee9-deploy,ee9-jsp,ee9-jstl

EXPOSE 8080
