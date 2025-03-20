package com.nhnacademy.taskapi.stock.controller;


import com.nhnacademy.taskapi.stock.domain.Stock;
import com.nhnacademy.taskapi.stock.dto.StockCreateUpdateDTO;
import com.nhnacademy.taskapi.stock.service.StockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/task/stock")
@Tag(name = "stock", description = "도서의 재고를 등록, 수정, 조회")
public class StockController {
    private final StockService stockService;


    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody StockCreateUpdateDTO dto){
        Stock stock = stockService.addStock(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(stock);
    }


    @PutMapping
    public ResponseEntity<Stock> modifyStock(@RequestBody StockCreateUpdateDTO dto){
        Stock stock = stockService.updateStock(dto);
        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Stock> getStock(@PathVariable("bookId") long bookId){
        Stock stock = stockService.getStock(bookId);
        return ResponseEntity.ok().body(stock);
    }
}
