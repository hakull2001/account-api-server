package com.example.demo.service.impl;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.demo.constants.RoleEnum;
import com.example.demo.dao.Account;
import com.example.demo.service.AccountAuthorizer;
import com.example.demo.service.IAccountService;

@Service("accountAuthorizer")
public class AccountAuthorizerImpl implements AccountAuthorizer {

	@Autowired
	private IAccountService accountService;

	@Override
	public boolean isAdmin(Authentication authentication) {
		return Arrays.toString(authentication.getAuthorities().toArray()).contains(RoleEnum.ADMIN);
	}

	@Override
	public boolean isEmployee(Authentication authentication) {
		return Arrays.toString(authentication.getAuthorities().toArray()).contains(RoleEnum.EMPLOYEE);
	}

	@Override
	public boolean isYourself(Authentication authentication, Long accountId) {
		User userAuth = (User) authentication.getPrincipal();
		Account account = accountService.findByUserName(userAuth.getUsername());
		if (!Objects.equals(account.getId(), accountId)) {
			throw new AccessDeniedException("Access denied");
		}
		return true;
	}

	@Override
	public boolean isManager(Authentication authentication) {
		return Arrays.toString(authentication.getAuthorities().toArray()).contains(RoleEnum.MANAGER);

	}

}
