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
	
	@NullOrNotEmpty(message = "username is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String userName;

	@NullOrNotEmpty(message = "firstName is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String firstName;

	@NullOrNotEmpty(message = "lastName is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String lastName;

	@NullOrNotEmpty(message = "password is invalid")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&_]{8,}$", message = "password is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String password;

	@IsIn(value = {RoleEnum.ADMIN, RoleEnum.EMPLOYEE, RoleEnum.MANAGER}, message = "role is invalid")
	private String role;

	@NullOrNotEmpty(message = "department id is invalid")
	private Long departmentId;
	
	@NullOrNotEmpty(message = "position id is invalid")
	private Long positionId;
}
