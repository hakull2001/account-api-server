package com.example.demo.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.ResetPasswordToken;
@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
	ResetPasswordToken findByToken(String token);
	
	boolean existsByToken(String token);
	
	@Transactional
	@Modifying
	@Query(" DELETE FROM ResetPasswordToken WHERE AccountID = :accountId")
	void deleteByAccountId(Long accountId);
	
	@Query("	SELECT 	token	"
			+ "	FROM 	ResetPasswordToken"
			+ " WHERE 	AccountID = :accountId")
	String findByAccountId(Long accountId);
}	
