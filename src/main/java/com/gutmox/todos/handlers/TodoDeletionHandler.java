package com.gutmox.todos.handlers;

import com.google.inject.Inject;
import com.gutmox.todos.api.TodosApi;
import com.gutmox.todos.api.impl.TodosApiImpl;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.ext.web.RoutingContext;

public class TodoDeletionHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoDeletionHandler.class);

    private TodosApi todosApi;

    @Inject
    public TodoDeletionHandler(TodosApiImpl todosApi) {
        this.todosApi = todosApi;
    }

    @Override
    public void handle(RoutingContext event) {

        String todoIdentifier = event.request().getParam("todoIdentifier");

        LOGGER.info("todoIdentifier: " + todoIdentifier);

        todosApi.delete(todoIdentifier)
                .doOnError(err ->{
                    event.response().putHeader("content-type", "application/json")
                            .end(new JsonObject().put("error", err.getMessage()).encode());
                })
                .subscribe(result -> {

            LOGGER.info("Result : " + result);

            event.response().putHeader("content-type", "application/json").end();
        });

    }
}
