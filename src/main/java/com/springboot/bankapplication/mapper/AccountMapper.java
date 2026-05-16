package com.springboot.bankapplication.mapper;

import com.springboot.bankapplication.dto.AccountDto;
import com.springboot.bankapplication.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountDto accountDto);
    AccountDto toDto(Account account);
}
