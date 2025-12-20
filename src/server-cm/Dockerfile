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
COPY server-cm/ server-cm/

# Build only the specific module and its dependencies
RUN mvn -B --no-transfer-progress -DskipTests clean package -pl server-cm -am

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.compiler,java.desktop,java.management,java.naming,java.net.http,java.scripting,java.security.jgss,java.security.sasl,java.sql,java.xml.crypto,jdk.unsupported,java.xml,jdk.zipfs,java.instrument \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

FROM alpine:latest

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=build /javaruntime $JAVA_HOME

WORKDIR /app

COPY --from=build /src/server-cm/target/lib/ ./lib/
COPY --from=build /src/server-cm/target/server-cm.jar ./server-cm.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server-cm.jar"]
