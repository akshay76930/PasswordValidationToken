package com.spring.project.DataValidation.CrudApplication.Response;

public class ApiResponse<T> {
    private T data;
    private String message;

    public ApiResponse(T data) {
        this.data = data;
        this.message = null;
    }

    public ApiResponse(String message) {
        this.message = message;
        this.data = null;
    }

    // Getters and Setters
    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
