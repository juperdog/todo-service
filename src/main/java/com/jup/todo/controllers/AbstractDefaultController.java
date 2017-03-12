package com.jup.todo.controllers;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jup.todo.response.Response;
import com.jup.todo.response.ResponseModel;

@RestController
@RequestMapping("/api/v1")
public abstract class AbstractDefaultController {

	@ExceptionHandler(value = ConstraintViolationException.class)
	public HttpEntity<ResponseModel> handleConstraintViolationException(
			ConstraintViolationException exception) {
		
		String msg = exception.getConstraintViolations().iterator().next().getMessageTemplate();
		return new ResponseModel(Response.ERROR, msg).build(HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = EntityNotFoundException.class)
	public HttpEntity<ResponseModel> handleEntityNotFoundException(EntityNotFoundException exception) {
		
		return new ResponseModel(Response.ERROR, exception.getMessage()).build(HttpStatus.NOT_FOUND);
	}
}
