FROM docker.io/library/jetty:12-jre21-eclipse-temurin
# WARNING DO NOT CHANGE WORKDIR or set it back to what it was before
# $JETTY_BASE must be correct before starting Jetty

COPY target/*.war /var/lib/jetty/webapps/ROOT.war

RUN java -jar "$JETTY_HOME/start.jar" --add-modules=ee9-deploy,ee9-jsp,ee9-jstl
