package com.gutmox.todos.auth.repositories.impl;

import com.gutmox.todos.config.VertxConfiguration;
import com.gutmox.todos.auth.repositories.AuthRepository;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.ext.mongo.MongoClient;
import rx.Single;

import javax.inject.Inject;

public class AuthRepositoryMongoImpl implements AuthRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRepositoryMongoImpl.class);

    private MongoClient mongoClient;

    private String collection = "auth_users";

    private String userName = "userName";

    private String mongoIdentifier = "_id";

    @Inject
    public AuthRepositoryMongoImpl() {

        this(VertxConfiguration.authMongoClient());
    }

    public AuthRepositoryMongoImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Single<String> save(JsonObject userAuth) {

        LOGGER.info("user to save  : " + userAuth);

        userAuth.put(mongoIdentifier, userAuth.getString(userName));

        return mongoClient.rxSave(collection, userAuth);
    }

    @Override
    public Single<JsonObject> find(String userName) {

        LOGGER.info(this.userName  + " :  " + userName);

        return mongoClient.rxFindOne(collection, new JsonObject().put(mongoIdentifier, userName), null);
    }
}
