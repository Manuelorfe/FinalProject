package com.example.FinalProject.services;

import com.example.FinalProject.dtos.TransactionDTO;
import com.example.FinalProject.models.accounts.*;
import com.example.FinalProject.models.users.AccountHolder;
import com.example.FinalProject.models.users.Role;
import com.example.FinalProject.repositories.accounts.*;
import com.example.FinalProject.repositories.users.AccountHolderRepository;
import com.example.FinalProject.repositories.users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;


@Service
public class AccountHolderService {

    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public AccountHolder createAccountHolder(AccountHolder accountHolder) {
        //Guarda el objeto accountholder en la BD que le pasamos como JSON
        AccountHolder accountHolder1 = accountHolderRepository.save(accountHolder);
        roleRepository.save(new Role("ACCOUNTHOLDER", accountHolder1));
        return accountHolder1;
    }

    public List<Account> getListAccountsService(Long id) {
        //Recupero el accountHolder de la base de datos y compruebo que existe
        AccountHolder accountHolder =  accountHolderRepository.findById(id)
               .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found"));

        //Devuelvo una lista de sus cuentas
        return accountHolder.getPrimaryAccounts();
    }

    public BigDecimal getBalance(Long id) {
        //Recupero el accountHolder de la base de datos y compruebo que existe
        AccountHolder accountHolder =  accountHolderRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found"));

        BigDecimal balance = new BigDecimal("0.0");

        //Recorro todas sus cuentas y voy sumando los balances
        for (Account primaryAccount : accountHolder.getPrimaryAccounts()) {
                balance = balance.add(primaryAccount.getBalance());
        }

        return balance;
    }

    public Transaction makeTransferenceService(TransactionDTO transactionDTO) {
        /*Verificar que la cuenta que envía sea suya y que tenga fondos , quitarle el importe
        Verificar que la cuenta que recibe exista , añadirle el importe*/

        //Recupero la cuenta de la BD y compruebo que la cuenta existe
        Account senderAccount = accountRepository.findById(transactionDTO.getAccountSenderId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        //Que la cuenta tiene fondos
        if(senderAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The account has not enough founds");

        //Recupero la cuenta de la BD y compruebo que la cuenta de destino existe
        Account receiverAccount = accountRepository.findById(transactionDTO.getAccountReceiverId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        //Compruebo que no le deje la cuenta en negativo (EN EL CASO DE QUE INTRODUZCA UN SIGNO NEGATIVO DELANTE DE LA CIFRA)
        if(senderAccount.getBalance().subtract(transactionDTO.getAmount()).compareTo(BigDecimal.ZERO) == -1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough founds on");
        }

        //Le quito el importe a la cuenta que envía y se lo sumo a la cuenta destino
        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDTO.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionDTO.getAmount()));

        //Guardo el objeto transaction en la BD
        Transaction transaction = transactionRepository.save(new Transaction(transactionDTO.getAmount(), senderAccount, transactionDTO.getName(), receiverAccount));

        //Guardo las cuentas con sus nuevos balances
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        return transaction;
    }
}
