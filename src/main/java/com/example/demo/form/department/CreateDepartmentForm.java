package com.example.demo.form.department;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import com.example.demo.constants.Common;
import com.example.demo.constants.DepartmentEnum;
import com.example.demo.validators.IsIn;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateDepartmentForm {
	@NotBlank
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String name;

	private Long totalMember;

	@NotBlank
	@IsIn(value = {DepartmentEnum.Dev, DepartmentEnum.PM, DepartmentEnum.ScrumMaster, DepartmentEnum.Test}, message = "Type is invalid")
	private String type;

	private List<@Valid Account> accounts;

	@Data
	@NoArgsConstructor
	public static class Account {
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
}
