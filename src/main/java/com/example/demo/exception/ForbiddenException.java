package com.example.demo.exception;

@SuppressWarnings("serial")
public class ForbiddenException extends RuntimeException {
	public ForbiddenException(String message) {
		super(message);
	}
}
