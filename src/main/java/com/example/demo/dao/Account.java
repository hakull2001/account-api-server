package com.example.demo.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.constants.StatusAccountEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`Account`")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "AccountID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Email", unique = true)
	private String email;

	@Column(name = "Username", unique = true)
	private String userName;

	@Column(name = "FirstName")
	private String firstName;

	@Column(name = "LastName")
	private String lastName;

	@Column(name = "password", length = 500)
	//@JsonIgnore
	private String password;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	Date createDate;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "`status`", nullable = false)
	private StatusAccountEnum status = StatusAccountEnum.NOT_ACTIVE;

	@Column(name = "role", nullable = false)
	private String role;

	@ManyToOne
	@JoinColumn(name = "DepartmentID")
	private Department department;
	
	@PrePersist
	public void prePresist() {
		if (this.role == null)
			this.role = "EMPLOYEE";
		this.password = new BCryptPasswordEncoder().encode(this.password);
	}
}
