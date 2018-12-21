package com.gutmox.todos.auth.repositories;

import io.vertx.core.json.JsonObject;
import rx.Single;

public interface AuthRepository {

    Single<String> save(JsonObject userAuth);

    Single<JsonObject> find(String userName);

}
