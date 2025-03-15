package com.talenttrack.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiResponseFactory {
    private static final Logger logger = LoggerFactory.getLogger(ApiResponseFactory.class);

    public static <T> ApiResponse<T> buildSuccessResponse(T data) {
        return new ApiResponse<>(data, "Success", true);
    }

    public static <T> ApiResponse<T> buildErrorResponse(String message) {
        logger.warn("Error: {}", message);
        return new ApiResponse<>(null, message, false);
    }
}
