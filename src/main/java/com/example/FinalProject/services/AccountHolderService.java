package com.example.FinalProject.services;

import com.example.FinalProject.dtos.TransactionDTO;
import com.example.FinalProject.models.accounts.*;
import com.example.FinalProject.models.users.AccountHolder;
import com.example.FinalProject.models.users.Role;
import com.example.FinalProject.repositories.accounts.*;
import com.example.FinalProject.repositories.users.AccountHolderRepository;
import com.example.FinalProject.repositories.users.RoleRepository;
import com.example.FinalProject.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
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
    @Autowired
    SavingAccountRepository savingAccountRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public AccountHolder createAccountHolder(AccountHolder accountHolder) {
        //Guarda el objeto accountholder en la BD que le pasamos como JSON
        accountHolder.setPassword(passwordEncoder.encode(accountHolder.getPassword()));
        AccountHolder accountHolder1 = accountHolderRepository.save(accountHolder);
        roleRepository.save(new Role("ACCOUNTHOLDER", accountHolder1));

        return accountHolder1;
    }

    public List<Account> getListAccountsService(String userName) {
        //Recupero el accountHolder de la base de datos y compruebo que existe
        //Lo hacemos con el findByUsername porque le pasamos el nombre y no la id como hacíamos antes para el @AuthenticationPrincipal
        //De esta manera aunque un Account Holder tenga permiso para entrar a la ruta no podrá ver la lista del usuario cambiando la id, se autenticará con el nombre
        AccountHolder accountHolder = accountHolderRepository.findByUsername(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found"));

        //Devuelvo una lista de sus cuentas
        return accountHolder.getPrimaryAccounts();


     /* Asi sería sin la @AuthenticationPrincipal pasándole por parámetro la ID
     AccountHolder accountHolder = accountHolderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found"));

        return accountHolder.getPrimaryAccounts();*/
    }

    public BigDecimal getBalance(String userName) {
        //Recupero el accountHolder de la base de datos y compruebo que existe
        AccountHolder accountHolder = accountHolderRepository.findByUsername(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found"));

        BigDecimal balance = new BigDecimal("0.0");
        BigDecimal newBalanceSavingAccount;
        BigDecimal newBalanceCreditCard;


        //Recorro todas sus cuentas y voy sumando los balances
        for (Account primaryAccount : accountHolder.getPrimaryAccounts()) {
            //Si la cuenta es SavingAccount por cada año que pase le sumo el interestRate.
            if (primaryAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) primaryAccount;
                if (Period.between(savingAccount.getLastInterestApplied(), LocalDate.now()).getYears() > 1) {
                    newBalanceSavingAccount = savingAccount.getInterestRate().multiply(savingAccount.getBalance())
                            .multiply(BigDecimal.valueOf(Period.between(savingAccount.getLastInterestApplied(), LocalDate.now()).getYears())).add(savingAccount.getBalance());
                    savingAccount.setBalance(newBalanceSavingAccount);
                    savingAccount.setLastInterestApplied(savingAccount.getLastInterestApplied().plusYears(Period.between(savingAccount.getLastInterestApplied(), LocalDate.now()).getYears()));
                    savingAccountRepository.save(savingAccount);
                }
            }
            //Si la cuenta es Credit Card por cada mes que pasa le resto el interestRate
            if (primaryAccount instanceof CreditCard) {
                CreditCard creditCard = (CreditCard) primaryAccount;
                if (Period.between(creditCard.getLastInterestApplied(), LocalDate.now()).getMonths() > 1) {
                    newBalanceCreditCard = creditCard.getBalance().subtract(creditCard.getInterestRate().multiply(creditCard.getBalance())
                            .multiply(BigDecimal.valueOf(Period.between(creditCard.getLastInterestApplied(), LocalDate.now()).getMonths())).divide(BigDecimal.valueOf(12)));
                    creditCard.setBalance(newBalanceCreditCard);
                    creditCard.setLastInterestApplied(creditCard.getLastInterestApplied().plusYears(Period.between(creditCard.getLastInterestApplied(), LocalDate.now()).getYears()));
                    creditCardRepository.save(creditCard);
                }
            }
            //Si la cuenta es Checking por cada mes que pasa le descuento el MonthlyMaintenanceFee
            if (primaryAccount instanceof CheckingAccount) {
                CheckingAccount checkingAccount = (CheckingAccount) primaryAccount;
                if (Period.between(checkingAccount.getLastMonthlyMaintenanceFee(), LocalDate.now()).getMonths() > 1) {
                    checkingAccount.setBalance(checkingAccount.getBalance().subtract(checkingAccount.getMONTHLY_MAINTENANCE_FEE()));
                    checkingAccount.setLastMonthlyMaintenanceFee(LocalDate.now());
                    checkingAccountRepository.save(checkingAccount);
                }
            }
            balance = balance.add(primaryAccount.getBalance());
        }

        return balance;
    }

    public Transaction makeTransferenceService(TransactionDTO transactionDTO, CustomUserDetails customUserDetails) {
        /*Verificar que la cuenta que envía sea suya y que tenga fondos , quitarle el importe
        Verificar que la cuenta que recibe exista , añadirle el importe*/

        Account senderAccount1 = null;

        AccountHolder sender = accountHolderRepository.findByUsername(customUserDetails.getUsername()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found/doesn't match"));

        for(Account a : sender.getPrimaryAccounts()){
            if(a.getId() == transactionDTO.getAccountSenderId()) senderAccount1 = a;
        }

        for(Account a : sender.getSecondaryAccounts()){
            if(a.getId() == transactionDTO.getAccountSenderId()) senderAccount1 = a;
        }

        if(senderAccount1 == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The account is not found in your list of accounts");


        //Recupero la cuenta de la BD y compruebo que la cuenta existe
       /* Account senderAccount = accountRepository.findById(transactionDTO.getAccountSenderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));*/



        //Que la cuenta tiene fondos
        if (senderAccount1.getBalance().compareTo(transactionDTO.getAmount()) < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The account has not enough founds");

        //Recupero la cuenta de la BD y compruebo que la cuenta de destino existe
        Account receiverAccount = accountRepository.findById(transactionDTO.getAccountReceiverId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        //Compruebo que el nombre que pasa sea el mismo que el  del propietario de la cuenta que recibe
        if(!transactionDTO.getName().equals(receiverAccount.getPrimaryOwner().getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The name doesn't match with the owner of the account receiver name ");
        }

        //Compruebo que no le deje la cuenta en negativo (EN EL CASO DE QUE INTRODUZCA UN SIGNO NEGATIVO DELANTE DE LA CIFRA)
        if (senderAccount1.getBalance().subtract(transactionDTO.getAmount()).compareTo(BigDecimal.ZERO) == -1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough founds");
        }

        //Le quito el importe a la cuenta que envía
        senderAccount1.setBalance(senderAccount1.getBalance().subtract(transactionDTO.getAmount()));

        //Compruebo que si baja por debajo del balance minimo le aplique el penalty fee tanto en CheckingAccount como en SavingAccount
        if(senderAccount1 instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) senderAccount1;
            if (senderAccount1.getBalance().compareTo(savingAccount.getMinimumBalance()) == -1){
                senderAccount1.setBalance(senderAccount1.getBalance().subtract(senderAccount1.getPENALTY_FEE()));
            }
        }
        if(senderAccount1 instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) senderAccount1;
            if (senderAccount1.getBalance().compareTo(checkingAccount.getMINIMUM_BALANCE()) == -1){
                senderAccount1.setBalance(senderAccount1.getBalance().subtract(senderAccount1.getPENALTY_FEE()));
            }
        }

        //Le sumo a la cuenta destino el importe
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionDTO.getAmount()));

        //Guardo el objeto transaction en la BD
        Transaction transaction = transactionRepository.save(new Transaction(transactionDTO.getAmount(), senderAccount1, transactionDTO.getName(), receiverAccount));

        //Guardo las cuentas con sus nuevos balances
        accountRepository.save(senderAccount1);
        accountRepository.save(receiverAccount);

        return transaction;
    }
}
