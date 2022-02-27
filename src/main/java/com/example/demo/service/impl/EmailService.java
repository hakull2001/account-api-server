package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.demo.constants.Common;
import com.example.demo.dao.Account;
import com.example.demo.repositories.RegistrationAccountTokenRepository;
import com.example.demo.repositories.ResetPasswordTokenRepository;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IEmailService;

@Component
public class EmailService implements IEmailService {

	@Value("${url.confirmUrl}")
	private String confirmationUrl;

	@Autowired
	private IAccountService accountService;

	@Autowired
	private RegistrationAccountTokenRepository accountTokenRepository;

	@Autowired
	private ResetPasswordTokenRepository resetPasswordTokenRepository;


	@Autowired
	private JavaMailSender mailSender;

	private void sendEmail(final String recipientEmail,final String subject,final String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(recipientEmail);
		message.setSubject(subject);
		message.setText(content);

		mailSender.send(message);
	}

	@Override
	public void sendRegistrationAccountConfirm(String email) {
		Account account = accountService.findByEmail(email);
		String token = accountTokenRepository.findByAccountId(account.getId());
		String url = confirmationUrl + Common.ACTIVE_ACCOUNT + token;
		sendEmail(email, Common.SUBJECT, Common.CONTENT + url);
	}

	@Override
	public void sendResetPasswordAccountConfirm(String email) {
		Account account = accountService.findByEmail(email);
		String token = resetPasswordTokenRepository.findByAccountId(account.getId());
		String url = confirmationUrl + Common.RESET_PASSWORD + token;
		sendEmail(email, Common.SUBJECT_RESET, Common.CONTENT_RESET_PASSWORD + url);
	}
}
