package com.jup.todo.response;


public enum Response {
    SUCCESS("success"),
    ERROR("error"),
    FAIL("fail");

    private final String content;

    Response(String content) {
        this.content = content;
    }
    
    public String getContent(){
    	return this.content;
    }
}
