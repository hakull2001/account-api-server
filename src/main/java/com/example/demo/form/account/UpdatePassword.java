package com.example.demo.form.account;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.demo.constants.Common;
import com.example.demo.validators.NullOrNotEmpty;

import lombok.Data;

@Data
public class UpdatePassword {
	private String oldPassword;
	
	@NullOrNotEmpty(message = "password is invalid")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&_]{8,}$", message = "password is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String newPassword;
	
	private String passwordConfirm;
}
