package com.gutmox.todos.vertx.tools;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.Consumer;

public class VerticlesRunner {

    private VerticlesRunner() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(VerticlesRunner.class);

    public static void run(Class clazz) {
        run(clazz, new VertxOptions().setClustered(false), null);
    }

    public static void run(Class clazz, VertxOptions options, DeploymentOptions deploymentOptions) {
        String verticleID = clazz.getName();

        System.setProperty("vertx.cwd", "/");
        Consumer<Vertx> runner = vertx -> {
            try {
                if (deploymentOptions != null) {
                    vertx.deployVerticle(verticleID, deploymentOptions);
                } else {
                    vertx.deployVerticle(verticleID);
                }
            } catch (Exception t) {
                LOGGER.error("Error", t);
            }
        };
        if (options.isClustered()) {
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                } else {
                    LOGGER.error("Error", res.cause());
                }
            });
        } else {
            Vertx vertx = Vertx.vertx(options);
            runner.accept(vertx);
        }
    }
}
