package com.gutmox.todos.api;

import com.gutmox.todos.api.domain.Todo;
import rx.Single;

import java.util.List;

public interface TodosApi {

    Single<List<Todo>> getAll();

    Single<String> create(Todo todo);

    Single<Todo> get(String todoIdentifier);

    Single<Todo> replace(String todoIdentifier, Todo todo);

    Single<Long> delete(String todoIdentifier);
}
