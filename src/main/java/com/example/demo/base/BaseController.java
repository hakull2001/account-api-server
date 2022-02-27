package com.example.demo.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.pagination.PaginateDTO;
import com.example.demo.dto.pagination.PaginationDTO;
import com.example.demo.dto.pagination.PaginationResponseDTO;

@Component
public class BaseController<T> {
	public ResponseEntity<?> resSuccess(T data) {
		Map<String, T> map = new HashMap<>();
		map.put("data", data);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(HttpStatus.OK.value(), "Success", map));
	}

	public ResponseEntity<?> resListSuccess(List<?> data) {
		Map<String, List<?>> map = new HashMap<>();
		map.put("data", data);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(HttpStatus.OK.value(), "Success", map));
	}

	public ResponseEntity<?> resPagination(PaginateDTO<?> paginateDTO) {
		PaginationDTO<List<?>> paginationDTO = new PaginationDTO<>(paginateDTO.getPageData().getContent(),
				paginateDTO.getPagination());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new PaginationResponseDTO<>(HttpStatus.OK.value(), "Success", paginationDTO));
	}
}
