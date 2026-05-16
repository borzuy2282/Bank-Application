package com.springboot.bankapplication.service;

import com.springboot.bankapplication.dto.AccountDto;
import com.springboot.bankapplication.dto.TransferFundDto;
import com.springboot.bankapplication.entity.Account;
import com.springboot.bankapplication.entity.Transaction;
import com.springboot.bankapplication.entity.TransactionType;
import com.springboot.bankapplication.exception.AccountNotFoundException;
import com.springboot.bankapplication.exception.InsufficientAmountException;
import com.springboot.bankapplication.exception.WrongTransferOperationException;
import com.springboot.bankapplication.mapper.AccountMapper;
import com.springboot.bankapplication.repository.AccountRepository;
import com.springboot.bankapplication.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountMapper accountMapper, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public AccountDto createAccount(AccountDto accountDto){
        Account account = accountMapper.toEntity(accountDto);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDto(savedAccount);
    }

    public AccountDto getAccount(Long id){
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account was not found!"));
        return accountMapper.toDto(account);
    }

    @Transactional
    public AccountDto deposit(Long id, double amount){
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account was not found!"));
        account.setBalance(account.getBalance() + amount);
        saveTransaction(account.getId(), amount, TransactionType.DEPOSIT);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDto(savedAccount);
    }

    @Transactional
    public AccountDto withdraw(Long id, double amount){
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account was not found!"));
        if(account.getBalance() < amount){
            throw new InsufficientAmountException("Insufficient amount!");
        }
        account.setBalance(account.getBalance() - amount);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDto(savedAccount);
    }

    public List<AccountDto> getAllAccounts(){
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toDto)
                .toList();
    }

    public void deleteAccount(Long id){
        accountRepository.deleteById(id);
    }

    @Transactional
    public void transferFunds(TransferFundDto transferFundDto){
        if(Objects.equals(transferFundDto.fromId(), transferFundDto.toId())) throw new WrongTransferOperationException("Transfer to yourself is not possible!");
        Account from = accountRepository
                .findById(transferFundDto.fromId())
                .orElseThrow(() -> new AccountNotFoundException("Account, which tries to send, was not found!"));
        Account to = accountRepository
                .findById(transferFundDto.toId())
                .orElseThrow(() -> new AccountNotFoundException("Account, which tries to obtain, was not found!"));
        if(from.getBalance() < transferFundDto.amount()){
            throw new InsufficientAmountException("Insufficient amount!");
        }
        from.setBalance(from.getBalance() - transferFundDto.amount());
        to.setBalance(to.getBalance() + transferFundDto.amount());
        accountRepository.save(from);
        accountRepository.save(to);
    }

    private void saveTransaction(Long accountId, double amount, TransactionType type){
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}
