package com.gutmox.todos.auth.handlers;

import com.google.inject.Inject;
import com.gutmox.todos.auth.api.AuthenticationApi;
import com.gutmox.todos.auth.api.impl.AuthenticationApiImpl;
import com.gutmox.todos.auth.domain.JWT;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.ext.web.RoutingContext;

public class LogoutHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutHandler.class);

    private AuthenticationApi authenticationApi;

    @Inject
    public LogoutHandler(AuthenticationApiImpl authenticationApi) {
        this.authenticationApi = authenticationApi;
    }

    @Override
    public void handle(RoutingContext event) {

        String authorization = event.request().headers().get(HttpHeaders.AUTHORIZATION);

        authenticationApi.logout(JWT.builder().withJwtToken(authorization).build())
                .doOnError(err ->{
                    event.response().putHeader("content-type", "application/json")
                            .end(new JsonObject().put("error", err.getMessage()).encode());
                })
                .subscribe(res ->{
            event.response().putHeader("content-type", "application/json").end();
        });
    }
}
