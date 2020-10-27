# OSLC 2020 Reference Implementation

> ⚠️ **This is a 🚧 work-in-progress 🚧 repository!** If you are interested in getting involved,
[join](https://github.com/oslc-op/oslc-admin/blob/master/CONTRIBUTING.md#online-meetings) one of
our weekly calls.

## Intro

RM/CM/QM/AM domains are covered.

## Getting started

```
cd src/server-rm/

mvn clean package
docker build -t refimpl-server-rm .
docker run -p 8800:8080 refimpl-server-rm
# OR
mvn clean jetty:run

cd ../server-cm/
mvn clean package
docker build -t refimpl-server-cm .
docker run -p 8801:8080 refimpl-server-cm
# OR
mvn clean jetty:run

cd ../server-qm/
mvn clean package
docker build -t refimpl-server-qm .
docker run -p 8802:8080 refimpl-server-cm
# OR
mvn clean jetty:run

cd ../server-am/
mvn clean package
docker build -t refimpl-server-am .
docker run -p 8803:8080 refimpl-server-cm
# OR
mvn clean jetty:run
```

## License

TBD