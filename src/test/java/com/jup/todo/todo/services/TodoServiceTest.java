package com.jup.todo.todo.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.springframework.data.domain.PageRequest;

import com.jup.todo.todo.entities.Todo;
import com.jup.todo.todo.repositories.TodoRepo;

public class TodoServiceTest {
	
	@InjectMocks
	TodoService todoService;
	
	@Mock
	TodoRepo todoRepo;
	
	Todo mockTodo;
	
	List<Todo> mockTodoList;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		mockTodo = new Todo();
		mockTodo.setId(1l);
		mockTodo.setTitle("mockTitle");
		mockTodo.setData("mockData");
		
		mockTodoList = new ArrayList<Todo>();
		mockTodoList.add(mockTodo);
	}
	
	@Test
	public void testGetTodoById_success(){
		when(todoRepo.findOne(anyLong())).thenReturn(mockTodo);
		Todo actual = todoService.getTodoById(1l);
		verify(todoRepo).findOne(1l);
		Assert.assertEquals(mockTodo, actual);
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void testGetTodoById_withNullObect(){
		when(todoRepo.findOne(anyLong())).thenReturn(null);
		Todo actual = todoService.getTodoById(1l);
		verify(todoRepo).findOne(1l);
	}
	
	@Test
	public void testGetTodoAll(){
		when(todoRepo.findAll()).thenReturn(mockTodoList);
		List<Todo> actual = todoService.getTodoAll();
		verify(todoRepo).findAll();
		Assert.assertEquals(mockTodoList, actual);
	}
	
	@Test
	public void testGetTodoPage(){
		TodoService todoServiceSpy = Mockito.spy(todoService);
		todoServiceSpy.getTodoPage(1, 20);
		verify(todoServiceSpy).generatePageRequest(1, 20);
	}
	
	@Test
	public void testSaveTodo(){
		ArgumentCaptor<Todo> argumentCaptor = ArgumentCaptor.forClass(Todo.class);
		when(todoRepo.save(argumentCaptor.capture())).thenReturn(mockTodo);
		
		todoService.saveTodo(mockTodo);
		
		Assert.assertEquals(null, argumentCaptor.getValue().getId());
		Assert.assertEquals(mockTodo.getTitle(), argumentCaptor.getValue().getTitle());
		Assert.assertEquals(mockTodo.getData(), argumentCaptor.getValue().getData());

		verify(todoRepo).save(any(Todo.class));
	}
	
	@Test
	public void testDeleteTodo(){
		when(todoRepo.findOne(anyLong())).thenReturn(mockTodo);
		Mockito.doNothing().when(todoRepo).delete(anyLong());
		
		todoService.deleteTodo(1l);
		
		verify(todoRepo).findOne(1l);
		verify(todoRepo).delete(1l);
	}
	
	@Test
	public void testDeleteTodo_noData(){
		when(todoRepo.findOne(anyLong())).thenReturn(null);
		Mockito.doNothing().when(todoRepo).delete(anyLong());
		
		todoService.deleteTodo(1l);
		
		verify(todoRepo).findOne(1l);
		verify(todoRepo, Mockito.never()).delete(1l);
	}
	
	@Test
	public void testUpdateTodoSuccess(){
		ArgumentCaptor<Todo> argumentCaptor = ArgumentCaptor.forClass(Todo.class);
		
		when(todoRepo.findOne(anyLong())).thenReturn(mockTodo);
		when(todoRepo.save(argumentCaptor.capture())).thenReturn(mockTodo);
		
		Todo updatedTodo = new Todo();
		updatedTodo.setId(1l);
		updatedTodo.setTitle("NewTitle");
		updatedTodo.setData("NewData");
		
		todoService.updateTodo(updatedTodo);
		
		verify(todoRepo).findOne(mockTodo.getId());
		verify(todoRepo).save(any(Todo.class));
		
		Todo actual = argumentCaptor.getValue();
		Assert.assertEquals(updatedTodo.getId(), actual.getId());
		Assert.assertEquals(updatedTodo.getTitle(), actual.getTitle());
		Assert.assertEquals(updatedTodo.getData(), actual.getData());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void testUpdateTodo_TodoNoId(){
		ArgumentCaptor<Todo> argumentCaptor = ArgumentCaptor.forClass(Todo.class);
		
		when(todoRepo.findOne(anyLong())).thenReturn(mockTodo);
		when(todoRepo.save(argumentCaptor.capture())).thenReturn(mockTodo);
		
		Todo updatedTodo = new Todo();
		updatedTodo.setId(null);
		updatedTodo.setTitle("NewTitle");
		updatedTodo.setData("NewData");
		
		todoService.updateTodo(updatedTodo);
		
		
		verify(todoRepo, Mockito.never()).findOne(mockTodo.getId());
		verify(todoRepo, Mockito.never()).save(any(Todo.class));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void testUpdateTodo_NoObject(){
		ArgumentCaptor<Todo> argumentCaptor = ArgumentCaptor.forClass(Todo.class);
		
		when(todoRepo.findOne(anyLong())).thenReturn(null);
		when(todoRepo.save(argumentCaptor.capture())).thenReturn(mockTodo);
		
		Todo updatedTodo = new Todo();
		updatedTodo.setId(1l);
		updatedTodo.setTitle("NewTitle");
		updatedTodo.setData("NewData");
		
		todoService.updateTodo(updatedTodo);
		
		
		verify(todoRepo).findOne(mockTodo.getId());
		verify(todoRepo, Mockito.never()).save(any(Todo.class));
	}
	@Test
	public void testGeneratePageRequest(){
		PageRequest actual = todoService.generatePageRequest(1, 20);
		Assert.assertEquals(1, actual.getPageNumber());
		Assert.assertEquals(20, actual.getPageSize());
	}
	
	@Test
	public void testGeneratePageRequest_numberAndSizeIsNull(){
		PageRequest actual = todoService.generatePageRequest(null, null);
		Assert.assertEquals(0, actual.getPageNumber());
		Assert.assertEquals(99, actual.getPageSize());
	}
}
