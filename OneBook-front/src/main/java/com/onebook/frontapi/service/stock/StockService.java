package com.onebook.frontapi.service.stock;

import com.onebook.frontapi.dto.stock.StockDTO;
import com.onebook.frontapi.feign.stock.StockClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockClient stockClient;

    public StockDTO getStock(long bookId){
        return stockClient.getStockByBookId(bookId);
    }
}
