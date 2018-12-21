package com.gutmox.todos.auth.domain;

import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class UserAuth {

    private JsonObject userAsJson;

    private UserAuth(JsonObject userAsJson) {
        this.userAsJson = userAsJson;
    }

    public static List<UserAuth> fromJson(List<JsonObject> jsonObjectList) {
        return jsonObjectList.stream().map(UserAuth::fromJson).collect(Collectors.toList());
    }

    public JsonObject toJson() {
        return this.userAsJson;
    }

    public static UserAuth fromJson(JsonObject jsonObject) {
        return new UserAuth(jsonObject);
    }

    public String getUserName() {
        return userAsJson.getString("userName");
    }
    public String getHashedPassword() {
        return userAsJson.getString("hashedPassword");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String userName, hashedPassword;

        private Builder() {
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withHashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }

        public UserAuth build() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("userName", userName);
            jsonObject.put("hashedPassword", hashedPassword);
            return new UserAuth(jsonObject);
        }
    }
}
