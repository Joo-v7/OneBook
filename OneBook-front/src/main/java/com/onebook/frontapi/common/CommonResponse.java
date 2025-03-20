package com.onebook.frontapi.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse<T> {
    private int status;
    private String message;
    private T data;

    public CommonResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public T getResult() {
        return this.data;
    }
}

//API 응답의 구조를 나타내는 클래스