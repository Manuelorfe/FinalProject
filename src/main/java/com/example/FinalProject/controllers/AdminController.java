package com.example.FinalProject.controllers;

import com.example.FinalProject.dtos.CheckingStudentDTO;
import com.example.FinalProject.models.accounts.Account;
import com.example.FinalProject.models.accounts.CreditCard;
import com.example.FinalProject.models.accounts.SavingAccount;
import com.example.FinalProject.models.users.AccountHolder;
import com.example.FinalProject.models.users.Address;
import com.example.FinalProject.models.users.ThirdPartyUser;
import com.example.FinalProject.models.users.User;
import com.example.FinalProject.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    // Area de ADMIN. A estos endpoint solo puede acceder el admin
    @Autowired
    AdminService adminService;


    @PostMapping("/add-third-party-user")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdPartyUser addThirdPartyUser(@RequestBody ThirdPartyUser thirdPartyUser){
        return adminService.addThirdPartyUserService(thirdPartyUser);
    }


    @PatchMapping("/change-balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account changeBalance(@PathVariable Long id, @RequestParam BigDecimal amount){
        return adminService.changeBalanceService(id,amount);
    }

    @GetMapping("/get-users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers(){
        return adminService.getAllUsers();
    }


    @PatchMapping("/add-mailing-adress-ah/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountHolder addMailingAddressAccountHolder(@PathVariable Long id, @RequestBody Address address){
        return adminService.addMailingAddressAccountHolder(id,address);
    }

    @PutMapping("/update-ah/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountHolder updateAccountHolder(@PathVariable Long id, @RequestBody AccountHolder accountHolder){
        return adminService.updateAccountHolderService(id,accountHolder);
    }

    @DeleteMapping("/remove-user/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void removeUser (@PathVariable Long id){
        adminService.removeUserService(id);
    }


    @PostMapping("/create-bank-account")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createCheckingAccount(@RequestBody CheckingStudentDTO checkingStudentDTO){
        return adminService.createCheckingAccountService(checkingStudentDTO);
    }

    @PostMapping("/create-saving-account")
    @ResponseStatus(HttpStatus.CREATED)
    public SavingAccount createSavingAccount(@RequestBody SavingAccount savingAccount){
        return adminService.createSavingAccountService(savingAccount);
    }

    @PostMapping("/create-credit-card")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard createCreditCard(@RequestBody CreditCard creditCard){
        return adminService.createCreditCardService(creditCard);
    }


}
