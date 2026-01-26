
# OSLC 2020 Reference Implementation

[![CI build](https://github.com/oslc-op/refimpl/actions/workflows/maven.yml/badge.svg)](https://github.com/oslc-op/refimpl/actions/workflows/maven.yml)
[![Docker](https://github.com/oslc-op/refimpl/actions/workflows/docker.yml/badge.svg)](https://github.com/oslc-op/refimpl/actions/workflows/docker.yml)
[![Discourse users](https://img.shields.io/discourse/users?color=28bd84&server=https%3A%2F%2Fforum.open-services.net%2F)](https://forum.open-services.net/)
[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/oslc-op/refimpl)


## Introduction

Reference implementation of four OSLC Servers covering each of the 4 OSLC Domains:
* [Requirements Management](https://oslc-op.github.io/oslc-specs/specs/rm/requirements-management-spec.html)
* [Change Management](https://oslc-op.github.io/oslc-specs/specs/cm/change-mgt-spec.html)
* [Quality Management](https://oslc-op.github.io/oslc-specs/specs/qm/quality-management-spec.html)
* [Architecture Management](https://oslc-op.github.io/oslc-specs/specs/am/architecture-management-spec.html)

## How to run
Follow the 3 sections below to:

1. [Run the OSLC Servers](#run-the-oslc-servers)
1. [Populate with sample data](#populate-with-sample-data)
1. [Navigate to OSLC servers](#navigate-to-oslc-servers)

## Run the OSLC Servers
There are multiple options to run the OSLC Server.

### Using built-in servers directly

This is the simplest option if you don't want to use anything except JDK and a Maven installation. Prerequisites:

- JDK 17
- Maven 3

Follow these steps to start 4 servers:

```sh
cd src/
mvn clean install
# if you have Docker running, you can run integration and acceptance tests
# mvn clean verify -Pacceptance

cd server-rm/
mvn clean jetty:run-war

cd ../server-cm/
mvn clean jetty:run-war

cd ../server-qm/
mvn clean jetty:run-war

cd ../server-am/
mvn clean jetty:run-war
```

After following these steps, proceed to the steps listed in the next section _Navigating OSLC servers_.

### Using Jetty-based containers with Docker

If you wish to run 4 OSLC servers as 4 containers similar to how one would deploy them in the cloud, you could follow steps listed in this subsection. Prerequisites:

- JDK 17 (only for non-Docker builds)
- Maven 3.9 (only for non-Docker builds)
- Docker CE 19 or equivalent
- Docker Compose v2

Docker Compose setup was also tested with Rancher Desktop 1.0.1.

```
cd src/
# Using Docker Compose
docker compose up --build
```

Make sure all is up and running:

```
$ docker ps
CONTAINER ID   IMAGE               COMMAND                  CREATED          STATUS          PORTS                    NAMES
7a719e58b909   refimpl-server-am   "/docker-entrypoint.…"   2 seconds ago    Up 1 second     0.0.0.0:8803->8080/tcp   oslc-refimpl-am
ea678fe26dbe   refimpl-server-qm   "/docker-entrypoint.…"   10 seconds ago   Up 10 seconds   0.0.0.0:8802->8080/tcp   oslc-refimpl-qm
e7cdcb427f78   refimpl-server-cm   "/docker-entrypoint.…"   19 seconds ago   Up 18 seconds   0.0.0.0:8801->8080/tcp   oslc-refimpl-cm
37b905ccc0a0   refimpl-server-rm   "/docker-entrypoint.…"   28 seconds ago   Up 27 seconds   0.0.0.0:8800->8080/tcp   oslc-refimpl-rm
```

After following these steps, proceed to the steps listed in the next section _Navigating OSLC servers_.

To see logs:

    docker logs oslc-refimpl-am -f

### Using Pre-built Docker Images

Pre-built Docker images are available from GitHub Container Registry for both x86_64 (amd64) and ARM64 architectures:

```bash
# Run individual servers with latest images
docker run -p 8800:8080 ghcr.io/oslc-op/refimpl/server-rm:latest
docker run -p 8801:8080 ghcr.io/oslc-op/refimpl/server-cm:latest
docker run -p 8802:8080 ghcr.io/oslc-op/refimpl/server-qm:latest
docker run -p 8803:8080 ghcr.io/oslc-op/refimpl/server-am:latest

# Or run specific versions
docker run -p 8800:8080 ghcr.io/oslc-op/refimpl/server-rm:0.3.0
docker run -p 8801:8080 ghcr.io/oslc-op/refimpl/server-cm:0.3.0
docker run -p 8802:8080 ghcr.io/oslc-op/refimpl/server-qm:0.3.0
docker run -p 8803:8080 ghcr.io/oslc-op/refimpl/server-am:0.3.0
```

> **Note:** Images are automatically rebuilt weekly to ensure the latest security updates from base images.


### Running on Tomcat in Docker

```bash
cd src/
docker compose -f docker-compose.tomcat.yml up --build
```

### Running on Tomcat via Maven


```sh
cd src/
mvn clean install
# if you have Docker running, you can run integration and acceptance tests
# mvn clean verify -Pacceptance

cd server-rm/
mvn clean package cargo:run -D"cargo.maven.containerId=tomcat10x" -Pwith-jstl-impl

cd ../server-cm/
mvn clean package cargo:run -D"cargo.maven.containerId=tomcat10x" -Pwith-jstl-impl

cd ../server-qm/
mvn clean package cargo:run -D"cargo.maven.containerId=tomcat10x" -Pwith-jstl-impl

cd ../server-am/
mvn clean package cargo:run -D"cargo.maven.containerId=tomcat10x" -Pwith-jstl-impl

```
## Populate with sample data

Once running, you can populate the reference implementation servers with some OSLC resources using one of these 2 alternatives:

* With a Kotlin plugin or IntelliJ: run the client under `src/client-toolchain/src/main/kotlin/co/oslc/refimpl/client/Main.kt`

* As a java program: run the client using `java -jar client-toolchain/target/client-toolchain.jar`.

Parameters:
1. Use `--help` to see all accepted arguments.
1. If you omit the argument, all 4 servers will be initialized.
1. You can initialize one or more servers with - for example - `--init-servers am,qm`.

## Navigate to OSLC servers

After that, OSLC servers are available at the following URLs:

- http://localhost:8800/services/catalog/singleton
- http://localhost:8801/services/catalog/singleton
- http://localhost:8802/services/catalog/singleton
- http://localhost:8803/services/catalog/singleton

### Configuring Base URLs

By default, the servers use `http://localhost:8080` as the base URL. You can override this using the `LYO_BASEURL` environment variable to configure the public-facing URL for each server. This is particularly useful when:
- Running behind a reverse proxy
- Deploying to a cloud environment with a custom domain
- Using non-standard ports or HTTPS

**Examples:**

For a reverse proxy deployment where the RM server is accessible at `https://mytoolchain.example.com:9443/refimpl/rm/`:
```bash
export LYO_BASEURL=https://mytoolchain.example.com:9443/refimpl/rm
```

When using Docker Compose, set environment variables for each service:
```yaml
services:
  server-rm:
    environment:
      - LYO_BASEURL=https://mytoolchain.example.com:9443/refimpl/rm
```

See the commented examples in `docker-compose.yml` for more details.

**Note:** The servlet path (e.g., `/services/`) is automatically appended to the base URL, so you should only specify the base path up to but not including `/services/`.

### OAuth Configuration

Currently, OAuth 1.0 with Basic fallback is enabled only on the OSLC RM server (port 8800). Use `admin:admin` credentials for Basic authentication. In order to enable OAuth support on other servers, uncomment the following lines under `src/main/webapp/WEB-INF/web.xml` of each server:

```xml
<filter>
    <display-name>CredentialsFilter</display-name>
    <filter-name>CredentialsFilter</filter-name>
    <filter-class>co.oslc.refimpl.rm.gen.servlet.CredentialsFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>CredentialsFilter</filter-name>
    <url-pattern>/services/*</url-pattern>
</filter-mapping>
```

Swagger endpoints are available at the following URIs (use `sp_single` as a `serviceProviderId` where required):

- http://localhost:8800/swagger-ui/index.jsp
- http://localhost:8801/swagger-ui/index.jsp
- http://localhost:8802/swagger-ui/index.jsp
- http://localhost:8803/swagger-ui/index.jsp


Root Services for the RM Server is under: http://localhost:8800/services/rootservices (similar for other servers).


## License

The Reference Implementation code and Lyo Designer model are licensed under the Apache License v2.0. See the LICENSE file for the full text of the license.
