package com.example.demo.event.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.demo.service.IEmailService;

@Component
public class SendRegistrationAccountConfirmEmailListerner
		implements ApplicationListener<OnSendRegistrationAccountConfirmViaEmail> {

	@Autowired
	private IEmailService emailService;

	@Override
	public void onApplicationEvent(OnSendRegistrationAccountConfirmViaEmail event) {
		sendConfirmViaEmail(event.getEmail());
	}

	private void sendConfirmViaEmail(String email) {
		emailService.sendRegistrationAccountConfirm(email);
	}
}
