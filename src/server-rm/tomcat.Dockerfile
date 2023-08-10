FROM docker.io/library/tomcat:9-jre17

# do not write log files, log everything to the Docker daemon
COPY config/tomcat-log.properties $CATALINA_BASE/conf/logging.properties
ENV CATALINA_OUT=/dev/null

COPY target/*.war /usr/local/tomcat/webapps/ROOT.war
