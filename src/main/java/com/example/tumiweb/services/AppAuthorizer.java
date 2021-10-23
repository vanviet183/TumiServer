package com.example.tumiweb.services;

import org.springframework.security.core.Authentication;

public interface AppAuthorizer {
    boolean authorize(Authentication authentication, String role, Object callerObj);
}
