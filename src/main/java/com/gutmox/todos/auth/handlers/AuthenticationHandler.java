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

import java.util.Optional;

public class AuthenticationHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHandler.class);

    private AuthenticationApi authenticationApi;

    @Inject
    public AuthenticationHandler(AuthenticationApiImpl authenticationApi) {
        this.authenticationApi = authenticationApi;
    }

    @Override
    public void handle(RoutingContext event) {

        Optional<String> authorisationToken = Optional.ofNullable(event.request().headers().get(HttpHeaders.AUTHORIZATION));
        if (! authorisationToken.isPresent()){
            errorPayload(event, "authorization token not indicated");
        }

        authenticationApi.authenticate(JWT.builder().withJwtToken(authorisationToken.get()).build()).doOnError(err ->{
            errorPayload(event, "authorization token not valid");
        }).subscribe( res -> event.next());
    }

    private void errorPayload(RoutingContext event, String errorMessage) {
        event.response().putHeader("content-type", "application/json")
                .end(new JsonObject().put("error", errorMessage).encode());
    }
}
