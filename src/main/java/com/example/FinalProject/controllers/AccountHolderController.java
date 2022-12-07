package com.example.FinalProject.controllers;

import com.example.FinalProject.dtos.TransactionDTO;
import com.example.FinalProject.models.accounts.*;
import com.example.FinalProject.models.users.AccountHolder;
import com.example.FinalProject.security.CustomUserDetails;
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


    @GetMapping("/my-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getListAccounts(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String userName = customUserDetails.getUsername();
        return accountHolderService.getListAccountsService(userName);
    }

    @GetMapping("/my-balance")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getBalance(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String userName = customUserDetails.getUsername();
        return accountHolderService.getBalance(userName);
    }


    @PostMapping("/transference")
    @ResponseStatus(HttpStatus.OK)
    public Transaction makeTransference (@RequestBody TransactionDTO transactionDTO){
       return accountHolderService.makeTransferenceService(transactionDTO);
    }
}
