package com.springboot.bankapplication.dto;

import lombok.Data;

public record AccountDto(
         Long id,
         String holderName,
         double balance
) {

}
