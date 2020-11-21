FROM tomcat:9-jdk8-corretto

COPY config/tomcat-log.properties $CATALINA_BASE/conf/logging.properties

# do not write log files
ENV CATALINA_OUT=/dev/null

COPY target/*.war /usr/local/tomcat/webapps/ROOT.war
