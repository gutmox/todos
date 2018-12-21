package com.gutmox.todos.api.repositories.mongo;

import com.gutmox.todos.api.repositories.TodoRepository;
import com.gutmox.todos.config.VertxConfiguration;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.ext.mongo.MongoClient;
import rx.Single;

import javax.inject.Inject;
import java.util.List;

public class TodoRepositoryMongoImpl implements TodoRepository{

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoRepositoryMongoImpl.class);

    private MongoClient mongoClient;

    private String collection = "todos";

    private String todoIdentifier = "todoIdentifier";

    private String mongoIdentifier = "_id";

    @Inject
    public TodoRepositoryMongoImpl() {

        this(VertxConfiguration.todoMongoClient());
    }

    public TodoRepositoryMongoImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public Single<String> save(JsonObject todo) {

        LOGGER.info("todo to save  : " + todo);

        return mongoClient.rxSave(collection, todo);
    }


    public Single<List<JsonObject>> getAll() {

        return mongoClient.rxFind(collection, new JsonObject());
    }

    public Single<JsonObject> find(String todoIdentifier) {

        LOGGER.info(this.todoIdentifier  + " :  " + todoIdentifier);

        return mongoClient.rxFindOne(collection, new JsonObject().put(mongoIdentifier, todoIdentifier), null);
    }

    public Single<JsonObject> replace(String todoIdentifier, JsonObject todo) {

        LOGGER.info(this.todoIdentifier  + " :  " + todoIdentifier);

        Single<JsonObject> jsonObjectSingle = mongoClient.rxFindOneAndReplace(collection, new JsonObject().put(mongoIdentifier, todoIdentifier), todo);

        return jsonObjectSingle;
    }

    public Single<Long> remove(String todoIdentifier) {

        LOGGER.info(this.todoIdentifier  + " :  " + todoIdentifier);

        return mongoClient.rxRemoveDocument(collection, new JsonObject().put(mongoIdentifier, todoIdentifier)).map(res -> {

            LOGGER.info("deleted items" + res.getRemovedCount());

            return res.getRemovedCount();
        });
    }
}
