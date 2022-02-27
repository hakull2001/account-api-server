package com.example.demo.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.demo.constants.DepartmentEnum;
import com.example.demo.validators.IsIn;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
	@NotBlank
	private String name;

	@NotBlank
	private Long totalMember;

	@IsIn(value = { DepartmentEnum.Dev, DepartmentEnum.PM, DepartmentEnum.ScrumMaster,
			DepartmentEnum.Test }, message = "type is in valid")
	private String type;

	@NotBlank
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	private List<AccountDto> accounts;

	@Data
	@NoArgsConstructor
	static class AccountDto {
		@JsonProperty("accountId")
		private Long id;
		private String userName;
	}
}
