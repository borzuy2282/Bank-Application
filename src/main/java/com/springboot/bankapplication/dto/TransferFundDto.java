package com.springboot.bankapplication.dto;

import java.math.BigDecimal;

public record TransferFundDto(
        Long fromId,
        Long toId,
        BigDecimal amount
) {
}
