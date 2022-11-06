FROM openjdk:11
VOLUME /tmp
EXPOSE 8092
ADD ./target/ms-bootcoin-movement-0.0.1-SNAPSHOT.jar ms-bootcoin-movement.jar
ENTRYPOINT ["java","-jar","/ms-bootcoin-movement.jar"]