package com.example.demo.service;

public interface IEmailService {
	//void sendEmail(String recipientEmail, String subject, String content);
	void sendRegistrationAccountConfirm(String email);
	
	void sendResetPasswordAccountConfirm(String email);
}
