package com.example.demo.filters;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class DepartmentFilter {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date minCreateDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date maxCreateDate;
	
}
