package com.gutmox.todos.auth.handlers.factory;

import com.google.inject.Inject;
import com.gutmox.todos.auth.handlers.AuthenticationHandler;
import com.gutmox.todos.auth.handlers.LoginHandler;
import com.gutmox.todos.auth.handlers.LogoutHandler;
import com.gutmox.todos.auth.handlers.SignUpHandler;

public class AuthHandlersFactory {

    @Inject
    private LoginHandler loginHandler;

    @Inject
    private LogoutHandler logoutHandler;

    @Inject
    private SignUpHandler signUpHandler;

    @Inject
    private AuthenticationHandler authenticationHandler;

    public LoginHandler getLoginHandler() {
        return loginHandler;
    }

    public LogoutHandler getLogoutHandler() {
        return logoutHandler;
    }

    public SignUpHandler getSignUpHandler() {
        return signUpHandler;
    }

    public AuthenticationHandler getAuthenticationHandler() {
        return authenticationHandler;
    }
}
