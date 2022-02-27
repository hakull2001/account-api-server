package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.RegistrationAccountToken;
@Repository
public interface RegistrationAccountTokenRepository extends JpaRepository<RegistrationAccountToken, Long> {
	public RegistrationAccountToken findByToken(String token);
	public boolean existsByToken(String token);
	
	@Transactional
	@Query("	SELECT 	token	"
			+ "	FROM 	RegistrationAccountToken"
			+ " WHERE 	AccountID = :accountId")
	public String findByAccountId(Long accountId);
}
