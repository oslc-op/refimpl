FROM jetty:10-jdk17-alpine-eclipse-temurin
# WARNING DO NOT CHANGE WORKDIR or set it back to what it was before
# $JETTY_BASE must be correct before starting Jetty

COPY target/*.war /var/lib/jetty/webapps/ROOT.war
