package com.jup.todo.todo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.jup.todo.todo.entities.Todo;

public interface TodoRepo extends CrudRepository<Todo, Long> {
	List<Todo> findAll();
	Page<Todo> findAll(Pageable pageable);
}
