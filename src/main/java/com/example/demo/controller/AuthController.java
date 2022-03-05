package com.example.demo.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.base.BaseController;
import com.example.demo.constants.Common;
import com.example.demo.dao.Account;
import com.example.demo.exception.AppException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.form.account.CreateAccountForm;
import com.example.demo.form.account.UpdateAccountForm;
import com.example.demo.models.AuthenticationRequest;
import com.example.demo.models.AuthenticationResponse;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IEmailService;
import com.example.demo.service.IFileService;
import com.example.demo.service.impl.MyUserDetailsService;
import com.example.demo.utils.FileManager;
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
	private IFileService fileService;
	
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
		final String token = jwtUtil.generateToken(userDetails);
		Account account = accountService.findByUserName(authenticationRequest.getUserName());
		return this.resSuccess(new AuthenticationResponse(token, account));
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<?> checkExistsByEmail(@PathVariable(value = "email") String email) {
		boolean checked = accountService.checkExistsByEmail(email);
		return new ResponseEntity<>(checked, HttpStatus.OK);
	}

	@GetMapping("/userName/{userName}")
	public ResponseEntity<?> checkExistsByUsername(@PathVariable(value = "userName") String userName) {
		boolean checked = accountService.checkExistsByUsername(userName);
		return new ResponseEntity<>(checked, HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid CreateAccountForm accountForm) {
		Account oldAccount = accountService.findByUserName(accountForm.getUserName());
		if (oldAccount != null)
			throw new AppException(Common.USERNAME_NOT_FOUND);
		accountService.createAccount(accountForm);
		return new ResponseEntity<>(Common.SEND_EMAIL, HttpStatus.OK);
	}

	@GetMapping("/active/{token}")
	public ResponseEntity<?> activeAccountViaEmail(@PathVariable(value = "token") String token) {
		accountService.activeAcount(token);
		return new ResponseEntity<>(Common.ACTIVE_SUCCESS, HttpStatus.OK);
	}

	@GetMapping("/forget-password")
	public ResponseEntity<?> resetPasswordViaEmail(@RequestParam(value = "email") String email) {
		accountService.createRequestResetPassword(email);
		return new ResponseEntity<>(Common.SEND_RESET_PASSWORD, HttpStatus.OK);
	}

	@GetMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam(value = "token") String token,
			@RequestParam(value = "newPassword") String newPassword) {
		accountService.resetPassword(token, newPassword);
		return new ResponseEntity<>(Common.RESET_PASSWORD_SUCCESS, HttpStatus.OK);
	}

	@PostMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestBody AuthenticationResponse authenticationResponse) {
		try {
			String token = authenticationResponse.getToken();
			String username = jwtUtil.extractUsername(token);
			UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
			if (jwtUtil.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			Account account = accountService.findByUserName(username);
			return this.resSuccess(new AuthenticationResponse(jwtUtil.generateToken(userDetails), account));
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	@PutMapping("/update-detail/{accountId}")
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication) || @accountAuthorizer.isYourself(authentication, #accountId)")
	public ResponseEntity<?> updateDetail(@RequestBody UpdateAccountForm accountForm,
			@PathVariable("accountId") Long accountId) {
		Account account = accountService.findById(accountId);
		if(account == null)
			throw new NotFoundException("Not found user");
		Account savedAccount = accountService.updateAccount(accountForm, account);
		return new ResponseEntity<>(savedAccount, HttpStatus.OK);
	}
	


	@PostMapping(value = "/image/{accountId}")
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication) || @accountAuthorizer.isYourself(authentication, #accountId)")
	public ResponseEntity<?> upLoadImage(@RequestParam(name = "image") MultipartFile image, @PathVariable("accountId") Long accountId) throws IOException {

		if (!new FileManager().isTypeFileImage(image)) {
			return new ResponseEntity<>("File must be image!", HttpStatus.UNPROCESSABLE_ENTITY);
		}

		return new ResponseEntity<String>(fileService.uploadImage(image, accountId), HttpStatus.OK);
	}
}
