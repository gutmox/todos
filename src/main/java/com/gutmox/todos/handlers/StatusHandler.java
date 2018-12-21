package com.gutmox.todos.handlers;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;

public class StatusHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext context) {
        context.response().putHeader("content-type", "application/json")
                .end(new JsonObject().put("status", "Show must go on").encode());
    }
}
