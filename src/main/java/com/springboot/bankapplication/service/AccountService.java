package com.springboot.bankapplication.service;

import com.springboot.bankapplication.dto.AccountDto;
import com.springboot.bankapplication.entity.Account;
import com.springboot.bankapplication.mapper.AccountMapper;
import com.springboot.bankapplication.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    public AccountService(AccountMapper accountMapper, AccountRepository accountRepository) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountDto createAccount(AccountDto accountDto){
        Account account = accountMapper.toEntity(accountDto);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDto(savedAccount);
    }
}
