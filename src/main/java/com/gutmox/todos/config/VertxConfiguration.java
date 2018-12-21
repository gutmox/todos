package com.gutmox.todos.config;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.mongo.MongoClient;

public class VertxConfiguration {

    private VertxConfiguration() {
    }

    private static Vertx vertx;

    public static synchronized MongoClient todoMongoClient() {
        if (vertx == null){
            return null;
        }
        return MongoClient.createNonShared(vertx, mongoConfig("todos"));
    }

    public static synchronized MongoClient authMongoClient() {
        if (vertx == null){
            return null;
        }
        return MongoClient.createNonShared(vertx, mongoConfig("auth"));
    }

    private static JsonObject mongoConfig(String dbName) {

        JsonObject config = new JsonObject();

        config.put("connection_string", "mongodb://localhost:27017");

        config.put("db_name", dbName);

        return config;
    }

    public static void setVertx(Vertx vertx) {
        VertxConfiguration.vertx = vertx;
    }

    public static Vertx getVertx() {
        return vertx;
    }
}