package com.springboot.bankapplication.mapper;

import com.springboot.bankapplication.dto.TransactionDto;
import com.springboot.bankapplication.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDto toDto(Transaction transaction);
    Transaction toEntity(TransactionDto transactionDto);
}
