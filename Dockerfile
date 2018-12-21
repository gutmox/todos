###
#
# To build:
#
#  docker build -t todos .
#
# To run:
#
#  docker run -t -i -p 8080:8080 todos
#
###

FROM java:8-jre

ENV DIST_FILE build/distributions/todos.tar

# Set the location of the verticles
ENV VERTICLE_HOME /opt/verticles

EXPOSE 8080

COPY $DIST_FILE $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME

RUN tar -xvf todos.tar

ENTRYPOINT ["sh", "-c"]
CMD ["./rest-mqtt-service/bin/todos verticles=8"]
