package com.onebook.frontapi.dto.order;

import com.onebook.frontapi.dto.book.BookOrderRequest;
import com.onebook.frontapi.dto.delivery.DeliveryRequest;
import com.onebook.frontapi.dto.packaging.PackagingRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderFormRequest {
    private List<BookOrderRequest> items;
    private DeliveryRequest delivery;
    private int packagingId;
}
