package com.gutmox.todos.auth.api.impl;

import com.google.inject.Inject;
import com.gutmox.todos.auth.api.AuthenticationApi;
import com.gutmox.todos.auth.api.jwt.JwtManager;
import com.gutmox.todos.auth.domain.JWT;
import com.gutmox.todos.auth.domain.User;
import com.gutmox.todos.auth.domain.UserAuth;
import com.gutmox.todos.auth.repositories.AuthRepository;
import com.gutmox.todos.auth.repositories.impl.AuthRepositoryMongoImpl;
import com.gutmox.todos.config.PropertiesManager;
import org.mindrot.jbcrypt.BCrypt;
import rx.Single;

public class AuthenticationApiImpl implements AuthenticationApi {

    private AuthRepository authRepository;

    private JwtManager jwtManager;

    @Inject
    public AuthenticationApiImpl(AuthRepositoryMongoImpl authRepository, JwtManager jwtManager) {
        this.authRepository = authRepository;
        this.jwtManager = jwtManager;
    }

    @Override
    public Single<JWT> login(User user) {
        return authRepository.find(user.getUserName()).map(res -> {
            UserAuth userAuth = UserAuth.fromJson(res);

            if (res == null ||
                    !BCrypt.checkpw(user.getPassword(), userAuth.getHashedPassword())) {

                throw new RuntimeException("Invalid Credentials");
            }

            return JWT.builder().withJwtToken(jwtManager.generateToken(userAuth.getUserName())).build();
        });
    }

    @Override
    public Single<JWT> signUp(User user) {
        return authRepository.save(UserAuth.builder()
                .withUserName(user.getUserName())
                .withHashedPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())).build().toJson())
                .flatMap(saved -> Single.just(JWT.builder().withJwtToken(jwtManager.generateToken(user.getUserName())).build()));

    }

    @Override
    public Single<Void> logout(JWT jwt) {
        jwtManager.authenticate(jwt.toJson()).subscribe(user ->
                jwtManager.blacklistToken(jwt.getJwtToken(), PropertiesManager.getInstance().getIntValue("jwt.expire")));
        return Single.just(null);
    }

    @Override
    public Single<Void> authenticate(JWT jwt) {
        return jwtManager.authenticate(jwt.toJson()).flatMap(authenticated ->  Single.just(null));
    }
}
