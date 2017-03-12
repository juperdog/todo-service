package com.jup.todo.todo.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.jup.todo.todo.entities.Todo;
import com.jup.todo.todo.services.TodoService;

public class TodoControllerTest {

	@InjectMocks
	TodoController todoController;
	
	@Mock
	TodoService todoService;
	
	MockMvc mvc;
	
	Todo mockTodo;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(todoController).build();
		
		mockTodo = new Todo();
		mockTodo.setId(1l);
		mockTodo.setTitle("mockTitle");
		mockTodo.setData("mockData");
	}
	
	@Test
	public void testGetPageWith200_NoPageAndSize() throws Exception{
		Page<Todo> page = new PageImpl<>(new ArrayList<Todo>());
		when(todoService.getTodoPage(null, null)).thenReturn(page);
		String url = "/api/v1/todos";
		mvc.perform(get(url))
			.andExpect(jsonPath("$.message", is("success")))
			.andExpect(status().isOk());
		verify(todoService).getTodoPage(null, null);
	}
	
	@Test
	public void testGetPageWith200_PageAndSize() throws Exception{
		Page<Todo> page = new PageImpl<>(new ArrayList<Todo>());
		when(todoService.getTodoPage(anyInt(), anyInt())).thenReturn(page);
		String url = "/api/v1/todos?number=2&size=10";
		mvc.perform(get(url))
			.andExpect(jsonPath("$.message", is("success")))
			.andExpect(status().isOk());
		verify(todoService).getTodoPage(2, 10);
	}
	
	@Test
	public void testGetWith200() throws Exception{
		when(todoService.getTodoById(anyLong())).thenReturn(mockTodo);
		String url = "/api/v1/todos/1";
		mvc.perform(get(url))
			.andExpect(jsonPath("$.message", is("success")))
			.andExpect(status().isOk());
		verify(todoService).getTodoById(1L);
	}
	
	@Test
	public void testGetWith404() throws Exception{
		when(todoService.getTodoById(anyLong())).thenThrow(new EntityNotFoundException());
		String url = "/api/v1/todos/1";
		mvc.perform(get(url))
			.andExpect(jsonPath("$.message", is("error")))
			.andExpect(status().isNotFound());
		verify(todoService).getTodoById(1L);
	}
	
	@Test
	public void testPostWith200() throws Exception{
		when(todoService.saveTodo(any(Todo.class))).thenReturn(mockTodo);
		String url = "/api/v1/todos";

		mvc.perform(post(url)
			.content("{\"title\":\"title\",\"data\":\"data\"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.message", is("success")))
			.andExpect(status().isOk());
		verify(todoService).saveTodo(any(Todo.class));
	}
	
	@Test
	public void testPostWith400() throws Exception{
		Set<ConstraintViolation<?>> violations = new HashSet<>();
		ConstraintViolation mock1 = Mockito.mock(ConstraintViolation.class);
		when(mock1.getMessageTemplate()).thenReturn("name should not be empty");
		violations.add(mock1);
		when(todoService.saveTodo(any(Todo.class))).thenThrow(new ConstraintViolationException(violations));
		String url = "/api/v1/todos";

		mvc.perform(post(url)
			.content("{\"data\":\"data\"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.message", is("error")))
			.andExpect(status().isBadRequest());
		verify(todoService).saveTodo(any(Todo.class));
	}
	
	@Test
	public void testPutWith200() throws Exception{
		when(todoService.updateTodo(any(Todo.class))).thenReturn(mockTodo);
		String url = "/api/v1/todos/2";

		mvc.perform(put(url)
			.content("{\"title\":\"title\",\"data\":\"data\"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.message", is("success")))
			.andExpect(status().isOk());
		verify(todoService).updateTodo(any(Todo.class));
	}
	
	@Test
	public void testPutWith404() throws Exception{
		when(todoService.updateTodo(any(Todo.class))).thenThrow(new EntityNotFoundException());
		String url = "/api/v1/todos/2";

		mvc.perform(put(url)
			.content("{\"title\":\"title\",\"data\":\"data\"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.message", is("error")))
			.andExpect(status().isNotFound());
		verify(todoService).updateTodo(any(Todo.class));
	}
	
	@Test
	public void testDelWith200() throws Exception{
		Mockito.doNothing().when(todoService).deleteTodo(anyLong());
		String url = "/api/v1/todos/1";
		mvc.perform(delete(url))
			.andExpect(jsonPath("$.message", is("success")))
			.andExpect(status().isOk());
		verify(todoService).deleteTodo(1L);
	}
	
	@Test
	public void testDelWith404() throws Exception{
		Mockito.doNothing().doThrow(new EntityNotFoundException()).when(todoService).deleteTodo(anyLong());
		String url = "/api/v1/todos/1";
		mvc.perform(delete(url))
			.andExpect(jsonPath("$.message", is("success")))
			.andExpect(status().isOk());
		verify(todoService).deleteTodo(1L);
	}
}
