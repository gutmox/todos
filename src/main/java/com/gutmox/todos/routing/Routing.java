package com.gutmox.todos.routing;

import com.google.inject.Inject;
import com.gutmox.todos.auth.handlers.factory.AuthHandlersFactory;
import com.gutmox.todos.handlers.factory.TodosHandlersFactory;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.handler.ResponseTimeHandler;

public class Routing {

    private static final Logger LOGGER = LoggerFactory.getLogger(Routing.class);

    private Router router;

    private TodosHandlersFactory todosHandlersFactory;

    private AuthHandlersFactory authHandlersFactory;

    @Inject
    public Routing(TodosHandlersFactory todosHandlersFactory, AuthHandlersFactory authHandlersFactory) {
        this.todosHandlersFactory = todosHandlersFactory;
        this.authHandlersFactory = authHandlersFactory;
    }

    private static String loginURI  = "/auth/login";

    private static String logoutURI = "/auth/logout";

    private static String signUpURI = "/auth/signup";

    private static String todoURI  = "/todos";

    private static String todoIdentifierURI  = todoURI + "/:todoIdentifier";

    private static String todoAsteriskURI  = todoURI + "/*";

    public Router getRouter() {

        long bodyLimit = 1024;

        router.route().handler(ResponseTimeHandler.create());

        router.get("/status").handler(todosHandlersFactory.getStatusHandler());


        todosRouting(bodyLimit);

        authRouting(bodyLimit);

        LOGGER.debug("Routing getRouter");
        return router;
    }

    private void todosRouting(long bodyLimit) {

        router.route(todoURI).handler(authHandlersFactory.getAuthenticationHandler());
        router.route(todoAsteriskURI).handler(authHandlersFactory.getAuthenticationHandler());
        router.get(todoURI).handler(todosHandlersFactory.getTodoListHandler());
        router.get(todoURI).handler(todosHandlersFactory.getTodoListHandler());
        router.get(todoURI).handler(todosHandlersFactory.getTodoListHandler());
        router.post(todoURI).handler(BodyHandler.create().setBodyLimit(bodyLimit * bodyLimit));
        router.post(todoURI).handler(todosHandlersFactory.getTodoCreationHandler());
        router.get(todoIdentifierURI).handler(todosHandlersFactory.getTodoFinderHandler());
        router.put(todoIdentifierURI).handler(BodyHandler.create().setBodyLimit(bodyLimit * bodyLimit));
        router.put(todoIdentifierURI).handler(todosHandlersFactory.getTodoModificationHandler());
        router.delete(todoIdentifierURI).handler(todosHandlersFactory.getTodoDeletionHandler());
    }

    private void authRouting(long bodyLimit) {

        router.post(loginURI).handler(BodyHandler.create().setBodyLimit(bodyLimit * bodyLimit));
        router.post(loginURI).handler(authHandlersFactory.getLoginHandler());
        router.get(logoutURI).handler(authHandlersFactory.getLogoutHandler());
        router.post(signUpURI).handler(BodyHandler.create().setBodyLimit(bodyLimit * bodyLimit));
        router.post(signUpURI).handler(authHandlersFactory.getSignUpHandler());
    }

    public void setRouter(Router router) {
        this.router = router;
    }
}
