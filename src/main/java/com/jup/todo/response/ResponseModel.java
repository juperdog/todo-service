package com.jup.todo.response;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseModel {
	protected Response message;
	protected Object data = "";

	public ResponseModel(Response response, Object data) {
		this.message = response;
		this.data = data;
	}

	public ResponseModel(Response response) {
		this.message = response;
	}
	
	public HttpEntity<ResponseModel> build(HttpStatus status) {
		return new ResponseEntity<ResponseModel>(this, status);
	}

	//getter
	public String getMessage() {
		return message.getContent();
	}
	
	public Object getData() { 
		return data;
	}
	
}
