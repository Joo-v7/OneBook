package com.onebook.frontapi.dto.order;

import com.onebook.frontapi.dto.book.BookOrderRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class OrderSolutionRequest {
    private static final long serialVersionUID = 1L; // 버전 관리를 위한 ID

    private String solutionType;
    private List<BookOrderRequest> items;
    @Setter
    private String reason;

    public OrderSolutionRequest(String solutionType, List<BookOrderRequest> items) {
        this.solutionType = solutionType;
        this.items = items;
    }
}
