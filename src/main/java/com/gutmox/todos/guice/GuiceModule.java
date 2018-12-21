package com.gutmox.todos.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Context;
import io.vertx.rxjava.core.Vertx;

import java.util.Properties;

public class GuiceModule extends AbstractModule {

    private final Context context;

    public GuiceModule(Vertx vertx) {
        this.context = vertx.getOrCreateContext();
    }

    @Override
    protected void configure() {
        Names.bindProperties(binder(), extractToProperties(context.config()));
    }

    private Properties extractToProperties(JsonObject config) {
        Properties properties = new Properties();
        config.getMap().keySet()
                .forEach((String key) -> properties.setProperty(key, "" + config.getValue(key)));
        return properties;
    }
}
