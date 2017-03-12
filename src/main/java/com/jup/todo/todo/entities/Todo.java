package com.jup.todo.todo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "todo")
public class Todo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;
	
	@Column(name = "title", nullable = false)
	@NotBlank(message="Title should not be blank or null")
	@NotFound
	private String title;
	
	@Column(name = "data", nullable = false)
	private String data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", title=" + title + ", data=" + data + "]";
	}
	
	@PrePersist
	public void prePersist(){
		
	}
}
