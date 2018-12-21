package com.gutmox.todos.auth.api.jwt;

import com.gutmox.todos.config.PropertiesManager;
import com.gutmox.todos.config.VertxConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.shareddata.SharedData;
import io.vertx.rxjava.ext.auth.User;
import io.vertx.rxjava.ext.auth.jwt.JWTAuth;
import rx.Single;

public class JwtManager {

    private SharedData sharedData;
    private JWTAuth authProvider;
    private JWTOptions jwtOptions;
    private static final String BLACKLIST_TOKENS = "blacklisted-tokens-map";

    public JwtManager() {

        this(VertxConfiguration.getVertx());
    }

    public JwtManager(Vertx vertx) {

        this.sharedData = vertx.sharedData();

        authProvider = JWTAuth.create(vertx, new JWTAuthOptions()
                .setKeyStore(new KeyStoreOptions()
                        .setType(PropertiesManager.getInstance().getValue("jwt.keystore.type"))
                        .setPassword(PropertiesManager.getInstance().getValue("jwt.keystore.password"))
                        .setPath(PropertiesManager.getInstance().getValue("jwt.keystore"))));

        jwtOptions = new JWTOptions()
                .setIssuer(PropertiesManager.getInstance().getValue("jwt.iss"))
                .addAudience(PropertiesManager.getInstance().getValue("jwt.aud"))
                .setExpiresInSeconds(PropertiesManager.getInstance().getIntValue("jwt.expire"));
    }

    public Single<User> authenticate(JsonObject authInfo) {
        String jwt = authInfo.getString("jwt");
        return checkifInBlacklist(jwt).flatMap(inBlacklist -> {
            if (inBlacklist.booleanValue()) {
                throw new RuntimeException("Not valid token");
            }
            return authProvider.rxAuthenticate(authInfo);
        });

    }

    public void blacklistToken(String token, long ttl) {
        putToAsynMap(token, ttl);
    }

    public String generateToken(String userName) {
        JsonObject userObj = new JsonObject()
                .put("userName", userName);
        return authProvider.generateToken(userObj, jwtOptions);
    }

    private void putToAsynMap(String token, long ttl) {

        sharedData.rxGetAsyncMap(BLACKLIST_TOKENS).subscribe(aMap -> {
            aMap.put(token, null, ttl, AsyncResult::mapEmpty);
        });
    }

    private Single<Boolean> checkifInBlacklist(String token) {

        return sharedData.rxGetAsyncMap(BLACKLIST_TOKENS).flatMap(res ->
                res.rxGet(token).map(event2 -> {
                    if (event2 != null) {
                        return true;
                    } else {
                        return false;
                    }
                }));

    }

    public void setSharedData(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    public JWTAuth getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(JWTAuth authProvider) {
        this.authProvider = authProvider;
    }
}
