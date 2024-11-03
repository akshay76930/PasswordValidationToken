package com.spring.project.DataValidation.CrudApplication.Exception;

@SuppressWarnings("serial")
public class EmailAlreadyExistsException extends RuntimeException {
	public EmailAlreadyExistsException(String message) {
		super(message);
	}
}
