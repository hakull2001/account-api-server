package com.example.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.example.demo.constants.RoleEnum;
import com.example.demo.dao.Department;
import com.example.demo.validators.IsIn;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDto {
	private Long id;
	@NotBlank
	@Email
	@Length(min = 6)
	private String email;

	@NotBlank
	@Length(min = 6)
	private String userName;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;
	
	@NotBlank
	private String password;
	
	private String avatarUrl;
	
	@IsIn(value = {RoleEnum.ADMIN, RoleEnum.EMPLOYEE, RoleEnum.MANAGER}, message = "role is invalid")
	private String role;

}
