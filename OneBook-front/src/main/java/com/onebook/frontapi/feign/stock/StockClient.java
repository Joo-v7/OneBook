package com.onebook.frontapi.feign.stock;

import com.onebook.frontapi.dto.stock.StockDTO;
import com.onebook.frontapi.dto.tag.CreateTagRequest;
import com.onebook.frontapi.dto.tag.TagResponse;
import com.onebook.frontapi.dto.tag.UpdateTagRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "stockClient", url = "${onebook.gatewayUrl}")
public interface StockClient {


    @GetMapping("/task/stock/{bookId}")
    StockDTO getStockByBookId(@PathVariable("bookId") Long bookId);
}