package com.gutmox.todos.api.repositories;

import io.vertx.core.json.JsonObject;
import rx.Single;

import java.util.List;

public interface TodoRepository {

    Single<String> save(JsonObject todo);

    Single<List<JsonObject>> getAll();

    Single<JsonObject> find(String todoIdentifier);

    Single<JsonObject> replace(String todoIdentifier, JsonObject todo);

    Single<Long> remove(String todoIdentifier);
}
