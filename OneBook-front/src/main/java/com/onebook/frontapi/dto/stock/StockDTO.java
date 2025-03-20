package com.onebook.frontapi.dto.stock;

import com.onebook.frontapi.dto.book.BookDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDTO {
    private long stockId;
    private BookDTO book;
    private int stock;
}
