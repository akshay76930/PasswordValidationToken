package com.spring.project.DataValidation.CrudApplication.Response;

/**
 * A generic response wrapper class that represents an API response.
 * 
 * @param <T> The type of the data being returned in the response.
 * 
 * <p>This class wraps the response data and a message to provide additional context 
 * (such as success or error messages) for the response returned by an API.</p>
 * 
 * <p>Author: Akshay Dhere &lt;akshaydhere14@gmail.com&gt;</p>
 */
public class ApiResponse<T> {

    // The data that is being returned in the API response
    private T data;
    
    // A message associated with the API response, typically indicating success or failure
    private String message;

    /**
     * Constructor to create an ApiResponse with data and a default success message.
     * 
     * @param data The data to be included in the response.
     */
    public ApiResponse(T data) {
        this.data = data;
        this.message = "Success"; // Default message set to "Success"
    }

    /**
     * Constructor to create an ApiResponse with a custom message.
     * 
     * @param message The custom message for the response.
     */
    public ApiResponse(String message) {
        this.message = message; // Allows setting a custom message, typically for errors or other responses
    }

    // Getter for the response data
    public T getData() {
        return data;
    }

    // Setter for the response data
    public void setData(T data) {
        this.data = data;
    }

    // Getter for the response message
    public String getMessage() {
        return message;
    }

    // Setter for the response message
    public void setMessage(String message) {
        this.message = message;
    }
}
