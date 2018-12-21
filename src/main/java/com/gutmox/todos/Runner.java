package com.gutmox.todos;


import com.gutmox.todos.verticles.MainVerticle;
import com.gutmox.todos.vertx.tools.VerticlesRunner;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

public class Runner {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        Integer verticlesNumber = 1;
        if (args != null && args.length > 0) {
            verticlesNumber = Integer.parseInt(args[0].split("=")[1]);
        }

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(verticlesNumber);
        deploymentOptions.setConfig(new JsonObject().put("http.port", PORT));
        VerticlesRunner.run(MainVerticle.class, new VertxOptions()
                .setClustered(false)
                .setEventLoopPoolSize(200)
                .setWorkerPoolSize(200)
            , deploymentOptions);
    }
}
