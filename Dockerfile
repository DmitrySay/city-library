FROM openjdk:11
WORKDIR /opt
COPY /build/libs/city-library.jar .
ENV JAVA_OPTS=""
CMD java ${JAVA_OPTS} -jar city-library.jar
