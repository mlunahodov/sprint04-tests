FROM gradle:8.10-jdk21 AS jre-build

RUN apt-get update && apt-get install -y curl unzip

WORKDIR /opt/app
COPY . /opt/app
RUN gradle bootJar --no-daemon


RUN /opt/java/openjdk/bin/jlink \
         --add-modules java.base,java.desktop,java.logging,java.sql,java.naming,java.management,java.security.jgss,java.instrument \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

# Copia o JAR gerado para um nome fixo
RUN cp build/libs/*.jar /opt/app/vroom.jar


FROM debian:buster-slim

ENV JAVA_HOME=/javaruntime
ENV PATH="$JAVA_HOME/bin:$PATH"

COPY --from=jre-build /javaruntime /javaruntime
COPY --from=jre-build /opt/app/vroom.jar /opt/app/vroom.jar

# Verifica se o executável java existe
RUN ls -l /javaruntime/bin/java

CMD ["/javaruntime/bin/java", "-jar", "/opt/app/vroom.jar"]
