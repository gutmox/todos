package com.gutmox.todos.auth.domain;

import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class User {

    private JsonObject userAsJson;

    private User(JsonObject userAsJson) {
        this.userAsJson = userAsJson;
    }

    public static List<User> fromJson(List<JsonObject> jsonObjectList) {
        return jsonObjectList.stream().map(User::fromJson).collect(Collectors.toList());
    }

    public JsonObject toJson() {
        return this.userAsJson;
    }

    public static User fromJson(JsonObject jsonObject) {
        return new User(jsonObject);
    }

    public String getUserName() {
        return userAsJson.getString("userName");
    }
    public String getPassword() {
        return userAsJson.getString("password");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String  userName, password;

        private Builder() {
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("userName", userName);
            jsonObject.put("password", password);
            return new User(jsonObject);
        }
    }
}
