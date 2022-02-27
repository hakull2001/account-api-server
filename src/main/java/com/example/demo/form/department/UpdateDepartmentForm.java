package com.example.demo.form.department;

import org.hibernate.validator.constraints.Length;

import com.example.demo.constants.Common;
import com.example.demo.constants.DepartmentEnum;
import com.example.demo.validators.IsIn;
import com.example.demo.validators.NullOrNotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateDepartmentForm {

	@NullOrNotEmpty(message = "name is invalid")
	@Length(max = Common.STRING_LENGTH_LIMIT)
	private String name;

	@NullOrNotEmpty(message = "total member is invalid")
	private Long totalMember;

	@NullOrNotEmpty(message = "type is invalid")
	@IsIn(value = { DepartmentEnum.Dev, DepartmentEnum.PM, DepartmentEnum.ScrumMaster,
			DepartmentEnum.Test }, message = "type is invalid")
	private String type;
}
