package com.example.demo.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.base.BaseController;
import com.example.demo.constants.Common;
import com.example.demo.dao.Account;
import com.example.demo.exception.AppException;
import com.example.demo.form.account.CreateAccountForm;
import com.example.demo.models.AuthenticationRequest;
import com.example.demo.models.AuthenticationResponse;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IEmailService;
import com.example.demo.service.impl.MyUserDetailsService;
import com.example.demo.utils.JwtUtil;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController extends BaseController<AuthenticationResponse> {
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private IAccountService accountService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUserName(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new AppException("Incorrect username or password");
		}
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUserName());
		final String jwt = jwtUtil.generateToken(userDetails);
		Account account = accountService.findByUserName(authenticationRequest.getUserName());
		return this
				.resSuccess(new AuthenticationResponse(jwt, account.getId(), account.getUserName(), account.getRole()));
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<?> checkExistsByEmail(@PathVariable(value = "email") String email){
		boolean checked = accountService.checkExistsByEmail(email);
		return new ResponseEntity<>(checked, HttpStatus.OK);
	}
	
	
	@GetMapping("/userName/{userName}")
	public ResponseEntity<?> checkExistsByUsername(@PathVariable(value = "userName") String userName){
		boolean checked = accountService.checkExistsByUsername(userName);
		return new ResponseEntity<>(checked, HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid CreateAccountForm accountForm) {
		Account oldAccount = accountService.findByUserName(accountForm.getUserName());
		if (oldAccount != null)
			throw new AppException(Common.USERNAME_NOT_FOUND);
		accountService.createAccount(accountForm);
//		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(newAccount.getUserName());
//		final String jwt = jwtUtil.generateToken(userDetails);
//		return this.resSuccess(
//				new AuthenticationResponse(jwt, newAccount.getId(), newAccount.getUserName(), newAccount.getRole()));
		return new ResponseEntity<>(Common.SEND_EMAIL, HttpStatus.OK);
	}
	
	@GetMapping("/active")
	public ResponseEntity<?> activeAccountViaEmail(@RequestParam(value = "token") String token){
		accountService.activeAcount(token);
		return new ResponseEntity<>(Common.ACTIVE_SUCCESS, HttpStatus.OK);
	}
	
	@GetMapping("/resetPassword")
	public ResponseEntity<?> resetPasswordViaEmail(@RequestParam(value = "email") String email){
		accountService.createRequestResetPassword(email);
		return new ResponseEntity<>(Common.SEND_RESET_PASSWORD, HttpStatus.OK);
	}
	
	@GetMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam(value = "token") String token, @RequestParam(value = "newPassword") String newPassword){
		accountService.resetPassword(token, newPassword);
		return new ResponseEntity<>(Common.RESET_PASSWORD_SUCCESS, HttpStatus.OK);
	}
}	
