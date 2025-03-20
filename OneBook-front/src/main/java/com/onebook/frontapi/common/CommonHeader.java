package com.onebook.frontapi.common;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonHeader {
    private final boolean isSuccessful;
    private final HttpStatus httpStatus;
    private final String resultMessage;     // Success or Error message

    @Builder
    public CommonHeader(HttpStatus httpStatus, String resultMessage) {
        httpStatus = Objects.isNull(httpStatus) ? HttpStatus.OK : httpStatus;
        resultMessage = Objects.isNull(resultMessage) ? httpStatus.getReasonPhrase() : resultMessage;

        this.isSuccessful = httpStatus.is2xxSuccessful();
        this.httpStatus = httpStatus;
        this.resultMessage = resultMessage;
    }
}
//HTTP 응답의 기본적인 정보(상태, 성공 여부, 메시지)를 캡슐화하는 역할