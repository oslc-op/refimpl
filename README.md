# 2019 Reference Implementation for RM/CM/QM/AM domains

**This is a work-in-progress repository!** If you are interested in getting involved,
[join](https://github.com/oslc-op/oslc-admin/blob/master/CONTRIBUTING.md#online-meetings) one of
our weekly calls.

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
```

## License

This program and the accompanying materials are made available under the terms of the Eclipse Public
License v1.0 and Eclipse Distribution License v. 1.0 which accompanies this distribution
([EDL license](LICENSE.EDL) and [EPL license](LICENSE)).