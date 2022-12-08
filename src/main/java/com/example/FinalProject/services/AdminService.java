package com.example.FinalProject.services;

import com.example.FinalProject.dtos.CheckingStudentDTO;
import com.example.FinalProject.models.accounts.*;
import com.example.FinalProject.models.users.*;
import com.example.FinalProject.repositories.accounts.*;
import com.example.FinalProject.repositories.users.AccountHolderRepository;
import com.example.FinalProject.repositories.users.RoleRepository;
import com.example.FinalProject.repositories.users.ThirdPartyUserRepository;
import com.example.FinalProject.repositories.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    StudentAccountRepository studentAccountRepository;
    @Autowired
    SavingAccountRepository savingAccountRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    ThirdPartyUserRepository thirdPartyUserRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    //todo: DTO para el PrimaryOwner de saving card y credit Card ?

    public ThirdPartyUser addThirdPartyUserService(ThirdPartyUser thirdPartyUser) {
            thirdPartyUser.setPassword(passwordEncoder.encode(thirdPartyUser.getPassword()));
            ThirdPartyUser thirdPartyUser1 = thirdPartyUserRepository.save(thirdPartyUser);
            roleRepository.save(new Role("THIRDPARTY", thirdPartyUser1));
            return thirdPartyUser1;
    }

    public Account changeBalanceService(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        account.setBalance(amount);
        return accountRepository.save(account);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void removeUserService(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.deleteById(user.getId());
    }


    public AccountHolder addMailingAddressAccountHolder(Long id, Address address) {

        AccountHolder accountHolder = accountHolderRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found"));

        accountHolder.setMailingAddress(address);

        return accountHolderRepository.save(accountHolder);
    }

    public AccountHolder updateAccountHolderService(Long id, AccountHolder accountHolder) {

        if(accountHolderRepository.findById(id).isPresent()){
            AccountHolder newAccountHolder = accountHolderRepository.findById(id).get();
            if(accountHolder.getUsername() != null) newAccountHolder.setUsername(accountHolder.getUsername());
            if(accountHolder.getPassword() != null) newAccountHolder.setPassword(accountHolder.getPassword());
            if(accountHolder.getName() != null) newAccountHolder.setName(accountHolder.getName());
            if(accountHolder.getDateOfBirth() != null) newAccountHolder.setDateOfBirth(accountHolder.getDateOfBirth());
            if(accountHolder.getPrimaryAddress() != null) newAccountHolder.setPrimaryAddress(accountHolder.getPrimaryAddress());
            if(accountHolder.getMailingAddress() != null) newAccountHolder.setMailingAddress(accountHolder.getMailingAddress());
            if(accountHolder.getPrimaryAccounts() != null) newAccountHolder.setPrimaryAccounts(accountHolder.getPrimaryAccounts());
            if(accountHolder.getSecondaryAccounts() != null) newAccountHolder.setSecondaryAccounts(accountHolder.getSecondaryAccounts());
            return accountHolderRepository.save(newAccountHolder);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El id especificado no se encuentra en la base de datos");
    }


    public Account createCheckingAccountService(CheckingStudentDTO checkingStudentDTO) {

        CheckingAccount checkingAccount = new CheckingAccount(
                checkingStudentDTO.getBalance(),
                accountHolderRepository.findById(checkingStudentDTO.getPrimaryOwnerId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found")),
                checkingStudentDTO.getSecretKey());

        long age = LocalDate.from(checkingAccount.getPrimaryOwner().getDateOfBirth())
                .until(LocalDate.now(), ChronoUnit.YEARS);

        if(age>24){
            if(checkingAccount.getBalance().compareTo(checkingAccount.getMINIMUM_BALANCE()) == -1){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The Balance must be greater than Minimum Balance");
            }
          return checkingAccountRepository.save(checkingAccount);
        }
        StudentAccount studentAccount = new StudentAccount(
                checkingStudentDTO.getBalance(),
                accountHolderRepository.findById(checkingStudentDTO.getPrimaryOwnerId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "AccountHolder not found")),
                checkingStudentDTO.getSecretKey());

        return studentAccountRepository.save(studentAccount);
    }


    public SavingAccount createSavingAccountService(SavingAccount savingAccount) {

        if(accountHolderRepository.findById(savingAccount.getPrimaryOwner().getId()).isPresent()){

            if(savingAccount.getBalance().compareTo(savingAccount.getMinimumBalance()) == -1){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The Balance must be greater than Minimum Balance");
            }
            System.err.println(savingAccount.getMinimumBalance());
            return savingAccountRepository.save(savingAccount);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Any USER with that ID");
    }

    public CreditCard createCreditCardService(CreditCard creditCard) {

        if(accountHolderRepository.findById(creditCard.getPrimaryOwner().getId()).isPresent()){

            if(creditCard.getBalance().compareTo(BigDecimal.valueOf(100)) == -1){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The Balance must be greater than Credit Limit Minimum");
            }
            return creditCardRepository.save(creditCard);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Any USER with that ID");
    }



}
