package com.onebook.frontapi.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Setter
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
