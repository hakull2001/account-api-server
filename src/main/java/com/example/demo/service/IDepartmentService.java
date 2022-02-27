package com.example.demo.service;

import java.util.List;

import com.example.demo.dao.Department;
import com.example.demo.dto.pagination.PaginateDTO;
import com.example.demo.form.department.CreateDepartmentForm;
import com.example.demo.form.department.UpdateDepartmentForm;
import com.example.demo.specification.GenericSpecification;

public interface IDepartmentService {

	Department findById(Long id);

	Department findByName(String name);
	
	void deleteById(Long id);

	PaginateDTO<Department> getList(Integer page, Integer perPage, GenericSpecification<Department> specification);

	Department create(CreateDepartmentForm createDepartmentForm);

	Department update(UpdateDepartmentForm departmentForm, Department currentDepartment) throws Exception;
	
	void deleteByIds(List<Long> id); 
}
