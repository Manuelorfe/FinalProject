package com.example.FinalProject.services;

import com.example.FinalProject.dtos.ThirdPartyTransactionDTO;
import com.example.FinalProject.models.accounts.*;
import com.example.FinalProject.models.users.ThirdPartyUser;
import com.example.FinalProject.repositories.accounts.AccountRepository;
import com.example.FinalProject.repositories.accounts.CheckingAccountRepository;
import com.example.FinalProject.repositories.accounts.SavingAccountRepository;
import com.example.FinalProject.repositories.accounts.StudentAccountRepository;
import com.example.FinalProject.repositories.users.ThirdPartyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class ThirdPartyService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    StudentAccountRepository studentAccountRepository;
    @Autowired
    SavingAccountRepository savingAccountRepository;
    @Autowired
    ThirdPartyUserRepository thirdPartyUserRepository;


    public Account makeThirdPartyTransference(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO) {

        CheckingAccount checkingAccount;
        StudentAccount studentAccount;
        SavingAccount savingAccount;


        ThirdPartyUser thirdPartyUser = thirdPartyUserRepository.findById(thirdPartyTransactionDTO.getThirdPartyId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "ThirdParty not found"));

        if(!hashedKey.equals(thirdPartyUser.getHashedKey())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your HashedKey is incorrect");
        }

        //Recupero la cuenta de la BD y compruebo que la cuenta existe
        Account accountReceiver = accountRepository.findById(thirdPartyTransactionDTO.getAccountId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));


        if(accountReceiver instanceof CreditCard) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't send money to a Credit Card");
        //Si es un tipo de cuenta Checking account
        if(accountReceiver instanceof CheckingAccount){
            checkingAccount = (CheckingAccount) accountReceiver;
            //Si la secret Key que introduces es la misma que la de la cuenta
            if(checkingAccount.getSecretKey().equals(thirdPartyTransactionDTO.getSecretKey())){
                //No puedes dejar la cuenta en saldo negativo, salta error
                if(checkingAccount.getBalance().add(thirdPartyTransactionDTO.getAmount()).compareTo(BigDecimal.ZERO) == -1){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough founds");
                }
                checkingAccount.setBalance(checkingAccount.getBalance().add(thirdPartyTransactionDTO.getAmount()));
                return checkingAccountRepository.save(checkingAccount);

            }
        }
        if(accountReceiver instanceof StudentAccount){
            studentAccount = (StudentAccount) accountReceiver;
            if(studentAccount.getSecretKey().equals(thirdPartyTransactionDTO.getSecretKey())){
                if(studentAccount.getBalance().add(thirdPartyTransactionDTO.getAmount()).compareTo(BigDecimal.ZERO) == -1){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough founds");
                }
                studentAccount.setBalance(studentAccount.getBalance().add(thirdPartyTransactionDTO.getAmount()));
                return studentAccountRepository.save(studentAccount);
            }
        }
        if(accountReceiver instanceof SavingAccount){
            savingAccount = (SavingAccount) accountReceiver;
            if(savingAccount.getSecretKey().equals(thirdPartyTransactionDTO.getSecretKey())){
                if(savingAccount.getBalance().add(thirdPartyTransactionDTO.getAmount()).compareTo(BigDecimal.ZERO) == -1){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough founds");
                }
                savingAccount.setBalance(savingAccount.getBalance().add(thirdPartyTransactionDTO.getAmount()));
                return savingAccountRepository.save(savingAccount);
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the transfer could not be made");
    }
}
