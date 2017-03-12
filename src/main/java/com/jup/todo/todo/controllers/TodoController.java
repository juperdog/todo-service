package com.jup.todo.todo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jup.todo.controllers.AbstractDefaultController;
import com.jup.todo.response.Response;
import com.jup.todo.response.ResponseModel;
import com.jup.todo.todo.entities.Todo;
import com.jup.todo.todo.services.TodoService;

@RestController
public class TodoController extends AbstractDefaultController{
	
	@Autowired
	TodoService todoService;

	@RequestMapping(value = "/todos", method = RequestMethod.GET)
	public HttpEntity<ResponseModel> getTodoList(
			@RequestParam(value = "number", required = false) Integer number,
			@RequestParam(value = "size", required = false) Integer size) {
		return new ResponseModel(Response.SUCCESS, todoService.getTodoPage(number, size)).build(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/todos/{id}", method = RequestMethod.GET)
	public HttpEntity<ResponseModel> getTodo(@PathVariable("id") long id) {
		return new ResponseModel(Response.SUCCESS, todoService.getTodoById(id)).build(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/todos", method = RequestMethod.POST)
	public HttpEntity<ResponseModel> saveTodo(@RequestBody @Valid Todo todo, BindingResult validResult) {
		return new ResponseModel(Response.SUCCESS, todoService.saveTodo(todo)).build(HttpStatus.OK);
	}

	@RequestMapping(value = "/todos/{id}", method = RequestMethod.PUT)
	public HttpEntity<ResponseModel> updateTodo(@RequestBody @Valid Todo todo, @PathVariable("id") long id, BindingResult validResult) {
		todo.setId(id);
		return new ResponseModel(Response.SUCCESS, todoService.updateTodo(todo)).build(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/todos/{id}", method = RequestMethod.DELETE)
	public HttpEntity<ResponseModel> delTodo(@PathVariable("id") long id) {
		todoService.deleteTodo(id);
		return new ResponseModel(Response.SUCCESS, "").build(HttpStatus.OK);
	}
}
