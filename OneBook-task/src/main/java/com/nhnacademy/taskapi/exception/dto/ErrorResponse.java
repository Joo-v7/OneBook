package com.nhnacademy.taskapi.exception.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ErrorResponse {
    @JsonProperty("title")
    private String title;

    @JsonProperty("status")
    private int status;

    @JsonProperty("timestamp")
    private Timestamp timestamp;

    public ErrorResponse(String title, int status) {
        this.title = title;
        this.status = status;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}
