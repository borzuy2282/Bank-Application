package com.springboot.bankapplication.controller;

import com.springboot.bankapplication.dto.AccountDto;
import com.springboot.bankapplication.exception.AccountNotFoundException;
import com.springboot.bankapplication.exception.InsufficientAmountException;
import com.springboot.bankapplication.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto){
        AccountDto createdAccountDto = accountService.createAccount(accountDto);
        return new ResponseEntity<>(createdAccountDto, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id){
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PutMapping("{id}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestBody Map<String, Double> request){
        if(!request.containsKey("amount")) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(accountService.deposit(id, request.get("amount")));
    }

    @PutMapping("{id}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable Long id, @RequestBody Map<String, Double> request){
        if(!request.containsKey("amount")) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(accountService.withdraw(id, request.get("amount")));
    }


    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAccountNotFoundException(AccountNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error:", ex.getMessage()));
    }

    @ExceptionHandler(InsufficientAmountException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientAmountException(InsufficientAmountException ex){
        return ResponseEntity
                .badRequest()
                .body(Map.of("error:", ex.getMessage()));
    }
}
