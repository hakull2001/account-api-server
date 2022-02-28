package com.example.demo.constants;

public class Common {
	public static final Integer PAGING_DEFAULT_LIMIT = 10;

	public static final int STRING_LENGTH_LIMIT = 255;

	public static final String SUBJECT = "Confirm Register Account ";

	public static final String SUBJECT_RESET = "Reset password Account";
	
	public static final String ACTIVE_ACCOUNT = "/api/v1/auth/active/";
	
	public static final String RESET_PASSWORD = "/auth/reset-password/";	

	public static final String CONTENT = "You have successfully registered an account, please click the link below to activate your account:\n";
	
	public static final String CONTENT_RESET_PASSWORD = "Please click the link below to reset password your account:\n";

	public static final String USERNAME_NOT_FOUND = "Username has already exists";

	public static final String SEND_EMAIL = "We have sent a email. Please check email to active account!";

	public static final String ACTIVE_SUCCESS = "Active account success !";
	
	public static final String RESET_PASSWORD_SUCCESS = "RESET PASSWORD SUCCESS";
	public static final String SEND_RESET_PASSWORD = "We have sent a email. Please check mail to reset password";
}
