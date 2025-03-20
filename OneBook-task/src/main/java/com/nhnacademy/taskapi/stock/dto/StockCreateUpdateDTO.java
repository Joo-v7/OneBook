package com.nhnacademy.taskapi.stock.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockCreateUpdateDTO {
    @NotNull
    private long bookId;
    @NotNull
    private int amount;
}
