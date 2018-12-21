package com.gutmox.todos.api.domain;

import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Todo {

    private JsonObject todoAsJson;

    private Todo(JsonObject todoAsJson) {
        this.todoAsJson = todoAsJson;
    }

    public String getTitle() {
        return todoAsJson.getString("title");
    }

    public String getDescription() {
        return todoAsJson.getString("description");
    }

    public Integer getPriority() {
        return todoAsJson.getInteger("priority");
    }

    public static List<Todo> fromJson(List<JsonObject> jsonObjectList) {
        return jsonObjectList.stream().map(Todo::fromJson).collect(Collectors.toList());
    }

    public JsonObject toJson() {
        return this.todoAsJson;
    }

    public static Todo fromJson(JsonObject jsonObject) {
        return new Todo(jsonObject);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String title, description;
        private Integer priority;

        private Builder() {
        }


        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withPriority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Todo build() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("title", title);
            jsonObject.put("description", description);
            jsonObject.put("priority", priority);
            return new Todo(jsonObject);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(todoAsJson, todo.todoAsJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(todoAsJson);
    }
}
