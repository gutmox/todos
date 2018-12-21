package com.gutmox.todos.auth.domain;

import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class JWT {

    private JsonObject jwtAsJson;

    private JWT(JsonObject jwtAsJson) {
        this.jwtAsJson = jwtAsJson;
    }

    public static List<JWT> fromJson(List<JsonObject> jsonObjectList) {
        return jsonObjectList.stream().map(JWT::fromJson).collect(Collectors.toList());
    }

    public JsonObject toJson() {
        return this.jwtAsJson;
    }

    public static JWT fromJson(JsonObject jsonObject) {
        return new JWT(jsonObject);
    }

    public String getJwtToken() {
        return jwtAsJson.getString("jwt");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String jwtToken;

        private Builder() {
        }

        public Builder withJwtToken(String jwtToken) {
            this.jwtToken = jwtToken;
            return this;
        }

        public JWT build() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("jwt", jwtToken);
            return new JWT(jsonObject);
        }
    }
}
