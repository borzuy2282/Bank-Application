package com.springboot.bankapplication.dto;

import com.springboot.bankapplication.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
        Long id,
        Long accountId,
        BigDecimal amount,
        TransactionType type,
        LocalDateTime timestamp) {
}
