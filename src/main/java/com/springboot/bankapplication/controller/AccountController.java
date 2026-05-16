package com.springboot.bankapplication.controller;

import com.springboot.bankapplication.dto.AccountDto;
import com.springboot.bankapplication.dto.TransactionDto;
import com.springboot.bankapplication.dto.TransferFundDto;
import com.springboot.bankapplication.exception.AccountNotFoundException;
import com.springboot.bankapplication.exception.ErrorDetails;
import com.springboot.bankapplication.exception.IncorrectRequestException;
import com.springboot.bankapplication.exception.InsufficientAmountException;
import com.springboot.bankapplication.exception.WrongTransferOperationException;
import com.springboot.bankapplication.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request){
        if(!request.containsKey("amount")) throw new IncorrectRequestException("Bad key name was provided");
        return ResponseEntity.ok(accountService.deposit(id, request.get("amount")));
    }

    @PutMapping("{id}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request){
        if(!request.containsKey("amount")) throw new IncorrectRequestException("Bad key name was provided");
        return ResponseEntity.ok(accountService.withdraw(id, request.get("amount")));
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id){
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFund(@RequestBody TransferFundDto transferFundDto){
        accountService.transferFunds(transferFundDto);
        return ResponseEntity.ok("Transfer was successful!");
    }

    @GetMapping("{id}/transactions")
    public ResponseEntity<List<TransactionDto>> getAccountTransactions(@PathVariable(name = "id") Long accountId){
        return ResponseEntity.ok(accountService.getAccountTransactions(accountId));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleAccountNotFoundException(AccountNotFoundException ex,
                                                                       WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                webRequest.getDescription(false),
                "ACCOUNT_NOT_FOUND"
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDetails);
    }

    @ExceptionHandler(InsufficientAmountException.class)
    public ResponseEntity<ErrorDetails> handleInsufficientAmountException(InsufficientAmountException ex,
                                                                                 WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                webRequest.getDescription(false),
                "INSUFFICIENT_AMOUNT"
        );
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(IncorrectRequestException.class)
    public ResponseEntity<ErrorDetails> handleIncorrectRequestException(IncorrectRequestException ex,
                                                                        WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                webRequest.getDescription(false),
                "INCORRECT_REQUEST"
        );
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(WrongTransferOperationException.class)
    public ResponseEntity<ErrorDetails> handleWrongTransferOperationException(WrongTransferOperationException ex,
                                                                              WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                webRequest.getDescription(false),
                "WRONG_TRANSFER_OPERATION"
        );
        return ResponseEntity.badRequest().body(errorDetails);
    }
}
