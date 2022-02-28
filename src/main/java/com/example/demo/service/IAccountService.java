package com.example.demo.service;

import java.util.List;

import com.example.demo.dao.Account;
import com.example.demo.dao.ResetPasswordToken;
import com.example.demo.dto.pagination.PaginateDTO;
import com.example.demo.form.account.CreateAccountForm;
import com.example.demo.form.account.UpdateAccountForm;
import com.example.demo.specification.GenericSpecification;

public interface IAccountService {
	Account findById(Long id);

	void createAccount(CreateAccountForm createAccountForm);

	Account findByUserName(String username);

	Account updateAccount(UpdateAccountForm updateAccountForm, Account currentAccount);

	PaginateDTO<Account> getList(Integer page, Integer perPage, GenericSpecification<Account> specification);

	Account createAdmin(Account account);

	void deleteByIds(List<Long> id);

	void deleteById(Long id);

	Account findByEmail(String email);
	
	boolean checkExistsByEmail(String email);
	
	boolean checkExistsByUsername(String userName);

	void activeAcount(String token);
			
	void resetPassword(String token, String newPassword);
				
	void deleteResetPasswordTokenByAccountId(Long accountId);
	
	void createNewResetPasswordAccountToken(Account account);
	
	void createRequestResetPassword(String email);
}
