package com.gutmox.todos.handlers.factory;

import com.google.inject.Inject;
import com.gutmox.todos.handlers.*;

public class TodosHandlersFactory {

    @Inject
    private StatusHandler statusHandler;

    @Inject
    private TodoListHandler todoListHandler;

    @Inject
    private TodoCreationHandler todoCreationHandler;

    @Inject
    private TodoModificationHandler todoModificationHandler;

    @Inject
    private TodoFinderHandler todoFinderHandler;

    @Inject
    private TodoDeletionHandler todoDeletionHandler;

    public StatusHandler getStatusHandler() {
        return statusHandler;
    }

    public TodoListHandler getTodoListHandler() {
        return todoListHandler;
    }

    public TodoCreationHandler getTodoCreationHandler() {
        return todoCreationHandler;
    }

    public TodoModificationHandler getTodoModificationHandler() {
        return todoModificationHandler;
    }

    public TodoFinderHandler getTodoFinderHandler() {
        return todoFinderHandler;
    }

    public TodoDeletionHandler getTodoDeletionHandler() {
        return todoDeletionHandler;
    }
}
