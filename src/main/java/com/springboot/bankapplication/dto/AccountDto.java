package com.springboot.bankapplication.dto;

import lombok.Data;

import java.math.BigDecimal;

public record AccountDto(
         Long id,
         String holderName,
         BigDecimal balance
) {

}
