package com.gutmox.todos.handlers;

import com.google.inject.Inject;
import com.gutmox.todos.api.TodosApi;
import com.gutmox.todos.api.impl.TodosApiImpl;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.ext.web.RoutingContext;

public class TodoFinderHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoFinderHandler.class);

    private TodosApi todosApi;

    @Inject
    public TodoFinderHandler(TodosApiImpl todosApi) {
        this.todosApi = todosApi;
    }

    @Override
    public void handle(RoutingContext event) {

        String todoIdentifier = event.request().getParam("todoIdentifier");

        LOGGER.info("todoIdentifier: " + todoIdentifier);

        todosApi.get(todoIdentifier)
                .doOnError(err ->{
                    event.response().putHeader("content-type", "application/json")
                            .end(new JsonObject().put("error", err.getMessage()).encode());
                })
                .subscribe(todo -> {

            LOGGER.info("Result : " + todo);

            event.response().putHeader("content-type", "application/json")
                    .end(todo.toJson().encode());
        });

    }
}
