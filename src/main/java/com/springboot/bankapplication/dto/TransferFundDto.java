package com.springboot.bankapplication.dto;

public record TransferFundDto(
        Long fromId,
        Long toId,
        double amount
) {
}
