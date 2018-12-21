package com.gutmox.todos.auth.handlers;

import com.google.inject.Inject;
import com.gutmox.todos.auth.api.AuthenticationApi;
import com.gutmox.todos.auth.api.impl.AuthenticationApiImpl;
import com.gutmox.todos.auth.domain.User;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.ext.web.RoutingContext;

public class LoginHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

    private AuthenticationApi authenticationApi;

    @Inject
    public LoginHandler(AuthenticationApiImpl authenticationApi) {
        this.authenticationApi = authenticationApi;
    }

    @Override
    public void handle(RoutingContext event) {

        LOGGER.info("Body : " + event.getBodyAsString());

        authenticationApi.login(User.fromJson(event.getBodyAsJson())).doOnError(err ->{
            event.response().putHeader("content-type", "application/json")
                    .end(new JsonObject().put("error", err.getMessage()).encode());
        }).subscribe(jwt ->{

            LOGGER.info("JWT : " + jwt.toJson());

            event.response().putHeader("content-type", "application/json")
                    .end(jwt.toJson().encode());
        });
    }
}
