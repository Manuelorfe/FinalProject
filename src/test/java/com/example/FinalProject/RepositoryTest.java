package com.example.FinalProject;

import com.example.FinalProject.models.accounts.*;
import com.example.FinalProject.models.users.AccountHolder;
import com.example.FinalProject.models.users.Address;
import com.example.FinalProject.models.users.Admin;
import com.example.FinalProject.models.users.ThirdPartyUser;
import com.example.FinalProject.repositories.accounts.*;
import com.example.FinalProject.repositories.users.AccountHolderRepository;
import com.example.FinalProject.repositories.users.AdminRepository;
import com.example.FinalProject.repositories.users.ThirdPartyUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    ThirdPartyUserRepository thirdPartyUserRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    StudentAccountRepository studentAccountRepository;
    @Autowired
    SavingAccountRepository savingAccountRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    TransactionRepository transactionRepository;

    Address address;
    AccountHolder accountHolder;
    ThirdPartyUser thirdPartyUser;
    Admin admin;
    CheckingAccount checkingAccount;
    StudentAccount studentAccount;
    SavingAccount savingAccount;
    CreditCard creditCard;
    Transaction transaction;

    @BeforeEach
    public void SetUp() {
        address = new Address("Calle 1", "Barcelona", "08019", "Spain");
        accountHolder = new AccountHolder("User1", "123456", "Manuel", LocalDate.of(1985, 02, 17), address);
        thirdPartyUser = new ThirdPartyUser("ThirdPartyUser", "1234", "Pepe", "HK0001");
        admin = new Admin("Admin2","0000", "AdminName");
        checkingAccount = new CheckingAccount(BigDecimal.valueOf(1000), accountHolder, "SKCHECKING");
        studentAccount = new StudentAccount(BigDecimal.valueOf(750), accountHolder, "SKSTUDENT");
        savingAccount = new SavingAccount(BigDecimal.valueOf(2500), accountHolder, "SKSAVING");
        creditCard = new CreditCard(BigDecimal.valueOf(2000), accountHolder);
        transaction = new Transaction(BigDecimal.valueOf(500), savingAccount, "Manuel", checkingAccount);
    }

    @AfterEach
    public void clean() {
        accountHolderRepository.deleteAll();
        thirdPartyUserRepository.deleteAll();
        adminRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        studentAccountRepository.deleteAll();
        savingAccountRepository.deleteAll();
        creditCardRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    public void shouldStoreAccountHolder() {
        accountHolderRepository.save(accountHolder);
        assertEquals(1, accountHolderRepository.findAll().size());
        AccountHolder accountHolderUserName = accountHolderRepository.findByUsername("User1").get();
        assertEquals("User1", accountHolderUserName.getUsername());
        assertTrue(accountHolderRepository.findByUsername("User1").isPresent());
        assertEquals("Manuel", accountHolderRepository.findByUsername("User1").get().getName());

    }

    @Test
    public void shouldStoreThirdPArtyUser() {
        thirdPartyUserRepository.save(thirdPartyUser);
        assertEquals(1, thirdPartyUserRepository.findAll().size());
    }

    @Test
    public void shouldStoreAdmin() {
        adminRepository.save(admin);
        assertEquals(1, adminRepository.findAll().size());
    }

    @Test
    public void shouldStoreCheckingAccount() {
        accountHolderRepository.save(accountHolder);
        checkingAccountRepository.save(checkingAccount);
        assertEquals(1, checkingAccountRepository.findAll().size());
    }

    @Test
    public void shouldStoreStudentAccount() {
        accountHolderRepository.save(accountHolder);
        studentAccountRepository.save(studentAccount);
        assertEquals(1, studentAccountRepository.findAll().size());
    }

    @Test
    public void shouldStoreSavingAccount() {
        accountHolderRepository.save(accountHolder);
        savingAccountRepository.save(savingAccount);
        assertEquals(1, savingAccountRepository.findAll().size());
    }

    @Test
    public void shouldStoreCreditCard() {
        accountHolderRepository.save(accountHolder);
        creditCardRepository.save(creditCard);
        assertEquals(1, creditCardRepository.findAll().size());
    }

    @Test
    public void shouldStoreTransaction() {
        accountHolderRepository.save(accountHolder);
        checkingAccountRepository.save(checkingAccount);
        savingAccountRepository.save(savingAccount);
        transactionRepository.save(transaction);
        assertEquals(1, transactionRepository.findAll().size());
    }

    @Test
    public void shouldDeleteAccountHolder(){
        accountHolderRepository.save(accountHolder);
        accountHolderRepository.deleteAll();
        assertEquals(0, accountHolderRepository.findAll().size());
    }

    @Test
    public void shouldModifyAccountHolder(){
       AccountHolder accountHolder1 = accountHolderRepository.save(accountHolder);
       accountHolder1.setUsername("UserNameModified");
       accountHolderRepository.save(accountHolder1);
       assertEquals("UserNameModified", accountHolderRepository.findByUsername("UserNameModified").get().getUsername());
    }


}
