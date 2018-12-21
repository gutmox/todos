package com.gutmox.todos.auth.api;

import com.gutmox.todos.auth.domain.JWT;
import com.gutmox.todos.auth.domain.User;
import rx.Single;

public interface AuthenticationApi {

    Single<JWT> login(User user);

    Single<JWT> signUp(User fromJson);

    Single<Void> logout(JWT jwt);

    Single<Void> authenticate(JWT jwt);
}