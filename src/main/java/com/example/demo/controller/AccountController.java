package com.example.demo.controller;

import java.util.List;

import javax.persistence.criteria.JoinType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.base.BaseController;
import com.example.demo.dao.Account;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.pagination.PaginateDTO;
import com.example.demo.exception.NotFoundException;
import com.example.demo.form.account.CreateAccountForm;
import com.example.demo.form.account.UpdateAccountForm;
import com.example.demo.service.impl.AccountService;
import com.example.demo.specification.GenericSpecification;
import com.example.demo.specification.JoinCriteria;
import com.example.demo.specification.SearchCriteria;
import com.example.demo.specification.SearchOperation;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin("*")
public class AccountController extends BaseController<Account> {
	@Autowired
	private AccountService accountService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication)")
	public ResponseEntity<?> getListAccounts(@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "perPage", required = false) Integer perPage,
			@RequestParam(name = "departmentName", required = false) String departmentName,
			@RequestParam(name = "userName", required = false) String userName,
			@RequestParam(name = "role", required = false) String role,
			HttpServletRequest request) {
		GenericSpecification<Account> specification = new GenericSpecification<Account>().getBasicQuery(request);
		if(userName != null) {
			specification.add(new SearchCriteria("userName", userName, SearchOperation.LIKE));
		}
		if(departmentName != null)
			specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "department", "name", departmentName, JoinType.LEFT));
		if(role != null)
			specification.add(new SearchCriteria("role", role, SearchOperation.EQUAL));
		PaginateDTO<Account> paginateAccounts = accountService.getList(page, perPage, specification);
		return this.resPagination(paginateAccounts);
	}

	@GetMapping("/{id}")
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication) || @accountAuthorizer.isYourself(authentication, #id)")
	public ResponseEntity<?> getAccountById(@PathVariable(value = "id") Long id) {
		Account account = accountService.findById(id);
		if (account == null)
			throw new NotFoundException("not found account with id " + id);
		AccountDto accountDto = modelMapper.map(account, AccountDto.class);
		return new ResponseEntity<>(accountDto, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication)")
	public ResponseEntity<?> createAccount(@RequestBody @Valid CreateAccountForm account) {
		accountService.createAccount(account);
		return new ResponseEntity<>("Create success", HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication) || @accountAuthorizer.isYourself(authentication, #id)")
	public ResponseEntity<?> updateAccount(@PathVariable("id") Long id,
			@RequestBody @Valid UpdateAccountForm accountUpdate) {
		Account account = accountService.findById(id);
		if (account == null)
			throw new NotFoundException("Not found user with id " + id);
		Account accountSaved = accountService.updateAccount(accountUpdate, account);
		return this.resSuccess(accountSaved);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication)")
    @Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> deleteAccount(@PathVariable(value = "id") Long id) {
		Account account = accountService.findById(id);
		if (account == null)
			throw new NotFoundException("not found account with id " + id);
		accountService.deleteById(id);
		return new ResponseEntity<>("Delete success", HttpStatus.OK);
	}
	@DeleteMapping
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication)")
	public void deleteAccounts(@RequestParam(name = "ids") List<Long> id) {
		accountService.deleteByIds(id);
	}

}
