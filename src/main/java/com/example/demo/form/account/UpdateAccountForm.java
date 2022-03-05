package com.example.demo.form.account;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.demo.constants.Common;
import com.example.demo.constants.RoleEnum;
import com.example.demo.validators.IsIn;
import com.example.demo.validators.NullOrNotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateAccountForm {

	@NullOrNotEmpty(message = "email is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String email;

	@NullOrNotEmpty(message = "firstName is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String firstName;

	@NullOrNotEmpty(message = "lastName is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String lastName;

	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&_]{8,}$", message = "password is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String password;

	@NullOrNotEmpty(message = "department id is invalid")
	private Long departmentId;
	
	@NullOrNotEmpty(message = "position id is invalid")
	private Long positionId;
	
	private String avatarUrl;
}
