package com.springboot.bankapplication.dto;

import lombok.Data;

@Data
public class AccountDto {
    private Long id;
    private String holderName;
    private double balance;
}
