package com.shop.product.interfaces.http.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 统一API响应封装
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
public class ApiResponse<T> {
    
    /**
     * 响应码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 响应时间戳
     */
    private LocalDateTime timestamp;
    
    /**
     * 请求追踪ID
     */
    private String traceId;
    
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        return response;
    }
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }
}
