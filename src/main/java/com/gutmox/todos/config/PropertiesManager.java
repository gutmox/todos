package com.gutmox.todos.config;

import com.gutmox.todos.auth.handlers.LoginHandler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public final class PropertiesManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

    private static final String PROP_FILE_NAME = "application.properties";

    public static final String RESOURCE_PATH = "src/main/resources/";

    private static PropertiesManager instance;

    private Properties properties;

    private PropertiesManager() {
        try {
            properties = new Properties();
            properties.load(FileUtils.openInputStream(new File(RESOURCE_PATH + PROP_FILE_NAME)));
        } catch (IOException ioe) {
            LOGGER.error("Error reading config properties: ", ioe);
            throw new RuntimeException(ioe);
        }
    }

    public static PropertiesManager getInstance() {
        if (instance == null)
            instance = new PropertiesManager();
        return instance;
    }

    public String getValue(String key) {
        String value = null;
        value = properties.getProperty(key);
        return value;
    }

    public Integer getIntValue(String key) {
        Integer value = null;
        try {
            value = Integer.valueOf(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
        return value;
    }
}
