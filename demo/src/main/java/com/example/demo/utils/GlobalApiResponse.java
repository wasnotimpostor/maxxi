package com.example.demo.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalApiResponse<T> {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String message = "success";
    private T data;

    public GlobalApiResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }


    public String toJson() {
        return new StringJoiner(", ", "{", "}")
                .add("\"timestamp\": \"" + timestamp + "\"")
                .add("\"status\": " + status)
                .add("\"error\": \"" + error + "\"")
                .add("\"message\": \"" + message + "\"")
                .add("\"data\": \"" + data + "\"")
                .toString();
    }

    public ResponseEntity<?> entity() {
        return ResponseEntity.status(status).headers(HttpHeaders.EMPTY).body(this);
    }
}

