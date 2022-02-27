package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
	Account findByUserName(String username);
	
	@Modifying
	@org.springframework.transaction.annotation.Transactional
	@Query("DELETE FROM Account WHERE id IN(:ids)")
	public void deleteByIds(@Param("ids") List<Long> id);
	
	Account findByEmail(String email);
	
	Account findStatusByEmail(String email);
}
