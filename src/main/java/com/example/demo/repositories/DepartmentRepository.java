package com.example.demo.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
	Department findByName(String name);
	
	@Modifying
	@org.springframework.transaction.annotation.Transactional
	@Query("DELETE FROM Department WHERE id IN(:ids)")
	public void deleteByIds(@Param("ids") List<Long> id);
}
                                        