package com.example.demo.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.base.BasePagination;
import com.example.demo.constants.StatusAccountEnum;
import com.example.demo.dao.Account;
import com.example.demo.dao.RegistrationAccountToken;
import com.example.demo.dao.ResetPasswordToken;
import com.example.demo.dto.pagination.PaginateDTO;
import com.example.demo.form.account.CreateAccountForm;
import com.example.demo.form.account.UpdateAccountForm;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.DepartmentRepository;
import com.example.demo.repositories.RegistrationAccountTokenRepository;
import com.example.demo.repositories.ResetPasswordTokenRepository;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IEmailService;
import com.example.demo.specification.GenericSpecification;
import com.example.demo.utils.JwtUtil;

@Service
@org.springframework.transaction.annotation.Transactional
@Component
public class AccountService extends BasePagination<Account, AccountRepository> implements IAccountService {
	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private IEmailService emailService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private RegistrationAccountTokenRepository accountTokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ResetPasswordTokenRepository resetPasswordTokenRepository;

	@Autowired
	public AccountService(AccountRepository accountRepository) {
		super(accountRepository);
	}

	@Override
	public void createAccount(CreateAccountForm createAccountForm) {
		Account account = modelMapper.map(createAccountForm, Account.class);
//		Department department = departmentRepository.findById(createAccountForm.getDepartmentId()).orElse(null);
//		System.out.println(department);
//		account.setDepartment(department);
		accountRepository.save(account);
		createNewRegistrationAccountToken(account);
		emailService.sendRegistrationAccountConfirm(account.getEmail());
	}

	@Override
	public Account updateAccount(UpdateAccountForm updateAccountForm, Account currentAccount) {
		Account updated = modelMapper.map(updateAccountForm, Account.class);
		updated.setId(currentAccount.getId());
		modelMapper.map(updated, currentAccount);
		if (updateAccountForm.getPassword() != null) {
			currentAccount.setPassword(passwordEncoder.encode(updateAccountForm.getPassword()));
		}
		return accountRepository.save(currentAccount);
	}

	@Transactional
	public void deleteById(Long id) {
		accountRepository.deleteById(id);
	}

	@Override
	public Account findByUserName(String username) {
		return accountRepository.findByUserName(username);
	}

	@Override
	public PaginateDTO<Account> getList(Integer page, Integer perPage, GenericSpecification<Account> specification) {
		return this.paginate(page, perPage, specification);
	}

	@Override
	public Account findById(Long id) {
		return accountRepository.findById(id).orElse(null);
	}

	@Override
	public Account createAdmin(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public void deleteByIds(List<Long> id) {
		accountRepository.deleteByIds(id);
	}

	@Override
	public Account findByEmail(String email) {
		return accountRepository.findByEmail(email);
	}

	private void createNewRegistrationAccountToken(Account account) {
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(account.getUserName());
		final String jwt = jwtUtil.generateToken(userDetails);

		RegistrationAccountToken tokenEntity = new RegistrationAccountToken(account, jwt);
		accountTokenRepository.save(tokenEntity);
	}

	@Override
	public void activeAcount(String token) {
		RegistrationAccountToken registrationAccountToken = accountTokenRepository.findByToken(token);
		Account account = registrationAccountToken.getAccount();
		account.setStatus(StatusAccountEnum.ACTIVE);

		accountRepository.save(account);

		accountTokenRepository.deleteById(registrationAccountToken.getId());
	}

	@Override
	public boolean checkExistsByEmail(String email) {
		Account account = accountRepository.findByEmail(email);
		if (account == null)
			return false;
		return true;
	}

	@Override
	public boolean checkExistsByUsername(String userName) {
		Account account = accountRepository.findByUserName(userName);
		if (account == null)
			return false;
		return true;
	}

	@Override
	public void resetPassword(String token, String newPassword) {
		ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken(token);
		Account account = resetPasswordToken.getAccount();
		account.setPassword(passwordEncoder.encode(newPassword));
		accountRepository.save(account);

		resetPasswordTokenRepository.deleteById(resetPasswordToken.getId());
	}

	@Override
	public void deleteResetPasswordTokenByAccountId(Long accountId) {
		resetPasswordTokenRepository.deleteByAccountId(accountId);
	}

	@Override
	public void createNewResetPasswordAccountToken(Account account) {
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(account.getUserName());
		final String jwt = jwtUtil.generateToken(userDetails);
		ResetPasswordToken tokenEntity = new ResetPasswordToken(account, jwt);
		resetPasswordTokenRepository.save(tokenEntity);
	}

	@Override
	public void createRequestResetPassword(String email) {
		Account account = findByEmail(email);
		deleteResetPasswordTokenByAccountId(account.getId());
		createNewResetPasswordAccountToken(account);
		emailService.sendResetPasswordAccountConfirm(email);
	}

}
