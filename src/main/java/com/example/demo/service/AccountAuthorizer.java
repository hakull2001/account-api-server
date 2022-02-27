package com.example.demo.service;

import org.springframework.security.core.Authentication;

public interface AccountAuthorizer {
	boolean isAdmin(Authentication authentication);

    boolean isManager(Authentication authentication);

    boolean isEmployee(Authentication authentication);

    boolean isYourself(Authentication authentication, Long accountId);
}
