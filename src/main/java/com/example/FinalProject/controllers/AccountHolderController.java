package com.example.FinalProject.controllers;

import com.example.FinalProject.dtos.TransactionDTO;
import com.example.FinalProject.models.accounts.*;
import com.example.FinalProject.models.users.AccountHolder;
import com.example.FinalProject.models.users.User;
import com.example.FinalProject.services.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/account-holder")
public class AccountHolderController {

    @Autowired
    AccountHolderService accountHolderService;


    @PostMapping("/create-user")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccountHolder(@RequestBody AccountHolder accountHolder){
        return accountHolderService.createAccountHolder(accountHolder);
    }


    @GetMapping("/my-accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getListAccounts(@PathVariable Long id, @AuthenticationPrincipal User user){

        return accountHolderService.getListAccountsService(id);
    }

    @GetMapping("/my-balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getBalance(@PathVariable Long id){
        return accountHolderService.getBalance(id);
    }


    @PostMapping("/transference")
    @ResponseStatus(HttpStatus.OK)
    public Transaction makeTransference (@RequestBody TransactionDTO transactionDTO){
       return accountHolderService.makeTransferenceService(transactionDTO);
    }
}
