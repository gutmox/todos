package com.gutmox.todos.api.impl;

import com.google.inject.Inject;
import com.gutmox.todos.api.TodosApi;
import com.gutmox.todos.api.domain.Todo;
import com.gutmox.todos.api.repositories.TodoRepository;
import com.gutmox.todos.api.repositories.mongo.TodoRepositoryMongoImpl;
import com.gutmox.todos.api.validations.TodoValidations;
import rx.Single;

import java.util.List;

public class TodosApiImpl implements TodosApi {

    private TodoRepository todoRepository;

    private TodoValidations todoValidations;

    @Inject
    public TodosApiImpl(TodoRepositoryMongoImpl todoRepository, TodoValidations todoValidations) {
        this.todoRepository = todoRepository;
        this.todoValidations = todoValidations;
    }

    @Override
    public Single<String> create(Todo todo) {
        return todoValidations.validate(todo).flatMap(isValid -> todoRepository.save(todo.toJson()));
    }

    @Override
    public Single<List<Todo>> getAll() {
        return todoRepository.getAll().map(Todo::fromJson);
    }

    @Override
    public Single<Todo> get(String todoIdentifier) {
        return todoRepository.find(todoIdentifier).map(Todo::fromJson);
    }

    @Override
    public Single<Todo> replace(String todoIdentifier, Todo todo) {
        return todoValidations.validate(todo).flatMap(isValid -> todoRepository.replace(todoIdentifier, todo.toJson()).map(Todo::fromJson));
    }

    @Override
    public Single<Long> delete(String todoIdentifier) {
        return todoRepository.remove(todoIdentifier);
    }
}
