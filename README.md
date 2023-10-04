
# OSLC 2020 Reference Implementation

![CI](https://github.com/oslc-op/refimpl/workflows/CI/badge.svg)
[![](https://img.shields.io/badge/talk-discourse-lightgrey.svg)](https://forum.open-services.net/)
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

- JDK 11
- Maven 3
- Docker CE 19 or equivalent
- (Optional) Docker Compose

Docker Compose setup was also tested with Rancher Desktop 1.0.1.

```
cd src/
mvn clean install

# Using Docker Compose
docker-compose up --build

# or, only using Docker
cd server-rm/
mvn clean package
docker build -t refimpl-server-rm .
docker run -d --name=oslc-refimpl-rm --rm -p 8800:8080 refimpl-server-rm

cd ../server-cm/
mvn clean package
docker build -t refimpl-server-cm .
docker run -d --name=oslc-refimpl-cm --rm -p 8801:8080 refimpl-server-cm

cd ../server-qm/
mvn clean package
docker build -t refimpl-server-qm .
docker run -d --name=oslc-refimpl-qm --rm -p 8802:8080 refimpl-server-qm

cd ../server-am/
mvn clean package
docker build -t refimpl-server-am .
docker run -d --name=oslc-refimpl-am --rm  -p 8803:8080 refimpl-server-am
cd ..
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

To stop:

    docker stop oslc-refimpl-am
    docker stop oslc-refimpl-cm
    docker stop oslc-refimpl-rm
    docker stop oslc-refimpl-qm

### Running on Tomcat in Docker

```bash
cd src/
mvn clean install

cd server-rm/
mvn clean package
docker build -f tomcat.Dockerfile -t refimpl-server-rm .
docker run -p 8800:8080 refimpl-server-rm
```

### Running on Tomcat via Maven


```sh
cd src/
mvn clean install
# if you have Docker running, you can run integration and acceptance tests
# mvn clean verify -Pacceptance

cd server-rm/
mvn cargo:run

cd ../server-cm/
mvn cargo:run

cd ../server-qm/
mvn cargo:run

cd ../server-am/
mvn cargo:run

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
