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

public class TodoCreationHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoCreationHandler.class);

    private TodosApi todosApi;

    @Inject
    public TodoCreationHandler(TodosApiImpl todosApi) {
        this.todosApi = todosApi;
    }

    @Override
    public void handle(RoutingContext event) {

        LOGGER.info("Body : " + event.getBodyAsString());

        todosApi.create(Todo.fromJson(event.getBodyAsJson()))
                .doOnError(err ->{
                    event.response().putHeader("content-type", "application/json")
                            .end(new JsonObject().put("error", err.getMessage()).encode());
                })
                .subscribe(todoIdentifier -> {

            LOGGER.info("Result : " + todoIdentifier);

            event.response().putHeader("content-type", "application/json")
                    .end(event.getBodyAsJson()
                            .put("todo_identifier", todoIdentifier)
                            .encode());
        });
    }
}
