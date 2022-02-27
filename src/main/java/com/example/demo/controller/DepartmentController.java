package com.example.demo.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.base.BaseController;
import com.example.demo.dao.Department;
import com.example.demo.dto.pagination.PaginateDTO;
import com.example.demo.exception.AppException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.form.department.CreateDepartmentForm;
import com.example.demo.form.department.UpdateDepartmentForm;
import com.example.demo.service.impl.DepartmentService;
import com.example.demo.specification.GenericSpecification;
import com.example.demo.specification.SearchCriteria;
import com.example.demo.specification.SearchOperation;

@RestController
@RequestMapping(value = "/api/v1/departments")
@CrossOrigin("*")
public class DepartmentController extends BaseController<Department> {

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication)")
	public ResponseEntity<?> getListDepartments(@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "perPage", required = false) Integer perPage,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "MnCreateDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date minDate,
			@RequestParam(name = "MxCreateDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date maxDate,
			HttpServletRequest request) {
		GenericSpecification<Department> specification = new GenericSpecification<Department>().getBasicQuery(request);
		if (name != null) {
			specification.add(new SearchCriteria("name", name, SearchOperation.LIKE));
		}
		if (type != null)
			specification.add(new SearchCriteria("type", type, SearchOperation.EQUAL));
		if (minDate != null)
			specification.add(new SearchCriteria("createDate", minDate, SearchOperation.GREATER_THAN_EQUAL));
		if (maxDate != null)
			specification.add(new SearchCriteria("createDate", maxDate, SearchOperation.LESS_THAN_EQUAL));
		PaginateDTO<Department> paginateDepartments = departmentService.getList(page, perPage, specification);
		return this.resPagination(paginateDepartments);
	}

	@PutMapping("/{id}")
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication)")
	public ResponseEntity<?> updateDepartment(@PathVariable(value = "id") Long id,
			@RequestBody @Valid UpdateDepartmentForm departmentUpdate) throws Exception {
		Department department = departmentService.findById(id);
		if (department == null)
			throw new NotFoundException("Not found department with id " + id);
		Department departmentSaved = departmentService.update(departmentUpdate, department);
		return this.resSuccess(departmentSaved);
	}

	@GetMapping("/{id}")
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication)")
	public ResponseEntity<?> getDepartmentByIdOrName(@PathVariable(value = "id") Object id) throws Exception {
		Department department;
		try {
			Long departmentId = Long.parseLong((String) id);
			department = departmentService.findById(departmentId);
		} catch (Exception e) {
			department = departmentService.findByName(id.toString());
		}

		return this.resSuccess(department);
	}

	@PostMapping
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication)")
	public ResponseEntity<?> createDepartment(@RequestBody @Valid CreateDepartmentForm departmentForm) {
		Department oldDepartment = departmentService.findByName(departmentForm.getName());
		if (oldDepartment != null) {
			throw new AppException("Department has already exists");
		}
		Department department = departmentService.create(departmentForm);
		return this.resSuccess(department);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication)")
	public ResponseEntity<?> deleteDepartment(@PathVariable(value = "id") Long id) throws Exception {
		Department department = departmentService.findById(id);
		if (department == null) {
			throw new NotFoundException("Not found department");
		}
		departmentService.deleteById(id);
		return this.resSuccess(department);
	}

	@DeleteMapping
	@PreAuthorize("@accountAuthorizer.isAdmin(authentication)")
	public void deleteDepartments(@RequestParam(name = "ids") List<Long> id) {
		departmentService.deleteByIds(id);
	}

}
