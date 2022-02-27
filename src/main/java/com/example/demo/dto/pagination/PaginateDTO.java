package com.example.demo.dto.pagination;

import org.springframework.data.domain.Page;

import com.example.demo.dto.pagination.PaginationDTO.Pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaginateDTO<T> {
	private Page<T> pageData;
	private Pagination pagination;
}