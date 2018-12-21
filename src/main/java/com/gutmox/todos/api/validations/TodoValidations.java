package com.gutmox.todos.api.validations;

import com.gutmox.todos.api.domain.Todo;
import rx.Single;

public class TodoValidations {

    public Single<Boolean> validate(Todo todo) {

        return Single.just(todo.getTitle().isEmpty() ||
                todo.getDescription().isEmpty() ||
                todo.getPriority() == null);
    }
}
