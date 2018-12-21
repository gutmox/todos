package com.gutmox.todos.handlers;

import com.google.inject.Inject;
import com.gutmox.todos.api.TodosApi;
import com.gutmox.todos.api.domain.Todo;
import com.gutmox.todos.api.impl.TodosApiImpl;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.util.stream.Collectors;

public class TodoListHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoListHandler.class);

    private TodosApi todosApi;

    @Inject
    public TodoListHandler(TodosApiImpl todosApi) {
        this.todosApi = todosApi;
    }

    @Override
    public void handle(RoutingContext event) {

        todosApi.getAll()
                .doOnError(err ->{
                    event.response().putHeader("content-type", "application/json")
                            .end(new JsonObject().put("error", err.getMessage()).encode());
                })
                .subscribe(result -> {

            LOGGER.info("Result : " + result);

            result.stream().map(Todo::toJson).collect(Collectors.toList());

            event.response().putHeader("content-type", "application/json")
                    .end();
        });
    }
}
