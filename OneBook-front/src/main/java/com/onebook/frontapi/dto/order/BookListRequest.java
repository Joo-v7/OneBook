package com.onebook.frontapi.dto.order;

import com.onebook.frontapi.dto.book.BookOrderRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookListRequest {
    private List<BookOrderRequest> bookOrderRequests;
}
