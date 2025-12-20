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

# Build Quarkus uber-jar (single JAR with all dependencies)
RUN mvn -B --no-transfer-progress -DskipTests clean package -pl server-rm -am -Dquarkus.package.jar.type=uber-jar

FROM registry.access.redhat.com/ubi8/openjdk-21-runtime:1.20

# Add metadata
LABEL org.opencontainers.image.title="OSLC RefImpl RM Server"
LABEL org.opencontainers.image.description="OSLC Requirements Management Reference Implementation"
LABEL org.opencontainers.image.source="https://github.com/oslc-op/refimpl"
LABEL org.opencontainers.image.vendor="OSLC Open Project"
LABEL org.opencontainers.image.licenses="EPL-2.0"

ENV LANGUAGE='en_US:en'

WORKDIR /deployments

# Copy the Quarkus uber-jar
COPY --from=build --chown=185 /src/server-rm/target/*-runner.jar /deployments/quarkus-run.jar

EXPOSE 8800
USER 185

ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]
