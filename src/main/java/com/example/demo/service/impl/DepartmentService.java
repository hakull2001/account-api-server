package com.example.demo.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.base.BasePagination;
import com.example.demo.dao.Department;
import com.example.demo.dto.pagination.PaginateDTO;
import com.example.demo.form.department.CreateDepartmentForm;
import com.example.demo.form.department.UpdateDepartmentForm;
import com.example.demo.repositories.DepartmentRepository;
import com.example.demo.service.IDepartmentService;
import com.example.demo.specification.GenericSpecification;

@Service
@org.springframework.transaction.annotation.Transactional
public class DepartmentService extends BasePagination<Department, DepartmentRepository> implements IDepartmentService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	public DepartmentService(DepartmentRepository departmentRepository) {
		super(departmentRepository);
	}

	@Override
	public Department update(UpdateDepartmentForm departmentForm, Department currentDepartment) throws Exception {
		Department updated = modelMapper.map(departmentForm, Department.class);
		updated.setId(currentDepartment.getId());
		modelMapper.map(updated, currentDepartment);
		return departmentRepository.save(currentDepartment);
	}

	@Override
	public Department create(CreateDepartmentForm createDepartmentForm) {
		Department department = modelMapper.map(createDepartmentForm, Department.class);
		return departmentRepository.save(department);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		departmentRepository.deleteById(id);
	}

	@Override
	public Department findById(Long id) {
		return departmentRepository.findById(id).orElse(null);
	}

	@Override
	public PaginateDTO<Department> getList(Integer page, Integer perPage,
			GenericSpecification<Department> specification) {
		return this.paginate(page, perPage, specification);
	}

	@Override
	public Department findByName(String name) {
		return departmentRepository.findByName(name);
	}

	@Override
	public void deleteByIds(List<Long> id) {
		departmentRepository.deleteByIds(id);
	}

}
