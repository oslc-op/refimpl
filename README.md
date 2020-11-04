# OSLC 2020 Reference Implementation

## Intro

RM/CM/QM/AM domains are covered.

## Getting started

```

cd src/
mvn clean install

cd server-rm/
mvn clean package

docker build -t refimpl-server-rm .
docker run -p 8800:8080 refimpl-server-rm
# OR
mvn clean jetty:run-exploded

cd ../server-cm/
mvn clean package

docker build -t refimpl-server-cm .
docker run -p 8801:8080 refimpl-server-cm
# OR
mvn clean jetty:run-exploded

cd ../server-qm/
mvn clean package

docker build -t refimpl-server-qm .
docker run -p 8802:8080 refimpl-server-cm
# OR
mvn clean jetty:run-exploded

cd ../server-am/
mvn clean package

docker build -t refimpl-server-am .
docker run -p 8803:8080 refimpl-server-cm
# OR
mvn clean jetty:run-exploded
```

After that, OSLC servers are available at the following URLs:

- http://localhost:8800/services/catalog/singleton
- http://localhost:8801/services/catalog/singleton
- http://localhost:8802/services/catalog/singleton
- http://localhost:8803/services/catalog/singleton

Root Services for the RM Server is under: http://localhost:8800/services/rootservices (similar for other servers).

In order to initialise resources in all reference implementation servers, use the client under `src/client-toolchain/src/main/kotlin/co/oslc/refimpl/client/Main.kt`

> Run the client after Maven build using `java -jar client-toolchain/target/client-toolchain-0.0.1-SNAPSHOT-jar-with-dependencies.jar` command in case you don't wish to work with Kotlin code directly.

## License

TBD