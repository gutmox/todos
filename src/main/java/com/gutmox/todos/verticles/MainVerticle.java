package com.gutmox.todos.verticles;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.gutmox.todos.config.VertxConfiguration;
import com.gutmox.todos.guice.GuiceModule;
import com.gutmox.todos.routing.Routing;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import rx.Observable;

public class MainVerticle extends io.vertx.rxjava.core.AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

    @Inject
    private Routing routing;

    @Override
    public void start(final Future<Void> startedResult) {
        startServer().subscribe(
                t -> {
                },
                t -> LOGGER.error(t.getMessage()),
                () -> {
                    LOGGER.info(MainVerticle.class.getName() + " Running on " + getPort() + " !!!!!!! ");
                    startedResult.complete();
                }
        );
    }

    private Observable<HttpServer> startServer() {
        VertxConfiguration.setVertx(vertx);
        Guice.createInjector(new GuiceModule(vertx)).injectMembers(this);
        HttpServerOptions options = new HttpServerOptions().setCompressionSupported(true);
        HttpServer httpServer = vertx.createHttpServer(options);
        Integer configuredPort = getPort();
        routing.setRouter(Router.router(vertx));
        return httpServer.requestHandler(routing.getRouter()::accept).rxListen(configuredPort).toObservable();
    }

    private Integer getPort() {
        return this.getVertx().getOrCreateContext().config().getInteger("http.port");
    }
}
