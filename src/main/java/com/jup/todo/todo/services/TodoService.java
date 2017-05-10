package com.jup.todo.todo.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.jup.todo.todo.entities.Todo;
import com.jup.todo.todo.repositories.TodoRepo;

@Service
public class TodoService {

	@Autowired
	private TodoRepo todoRepo;
	
	public Todo getTodoById(Long id){
		Todo todo = todoRepo.findOne(id);
		if(todo != null)
			return todo;
		else
			throw new EntityNotFoundException("Todo["+id+"] Not Found");
	}
	
	public List<Todo> getTodoAll(){
		return todoRepo.findAll();
	}
	
	public Page<Todo> getTodoPage(Integer number, Integer size){
		return todoRepo.findAll(generatePageRequest(number, size));
	}
	
	public Todo saveTodo(Todo todo){
		todo.setId(null);
		return todoRepo.save(todo);
	}
	
	public Todo updateTodo(Todo todo){
		if(todo.getId() == null) throw new EntityNotFoundException("Todo[] Not Found");
		Todo qObj = todoRepo.findOne(todo.getId());
		if(qObj != null){
			qObj.setTitle(todo.getTitle());
			qObj.setData(todo.getData());
			qObj.setStatus(todo.getStatus());
			return todoRepo.save(qObj);
		}else{
			throw new EntityNotFoundException("Todo["+todo.getId()+"] Not Found");
		}
	}
	
	public Todo updateStatus(Long id, String status){
		Todo qObj = todoRepo.findOne(id);
		if(qObj != null){
			qObj.setStatus(status);
			return todoRepo.save(qObj);
		}else{
			throw new EntityNotFoundException("Todo["+id+"] Not Found");
		}
	}
	
	public void deleteTodo(Long id){
		Todo qObj = todoRepo.findOne(id);
		if(qObj != null) {
			todoRepo.delete(id);
		}
	}
	
	public PageRequest generatePageRequest(Integer number, Integer size){
		if(number == null) number = 0;
		if(size == null) size = 99;
		return new PageRequest(number, size);
	}
}
