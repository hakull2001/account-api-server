package com.example.demo.form.account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.demo.constants.Common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateAccountForm {

	@Email
	@NotBlank
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String email;
	
	@NotBlank
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String userName;

	@NotBlank
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String firstName;

	@NotBlank
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String lastName;

	@NotBlank
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&_]{8,}$", message = "password is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String password;
	
	private Long departmentId;	
}
