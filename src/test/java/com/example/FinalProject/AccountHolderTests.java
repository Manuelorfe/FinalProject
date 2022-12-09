package com.example.FinalProject;

import com.example.FinalProject.dtos.TransactionDTO;
import com.example.FinalProject.models.accounts.CheckingAccount;
import com.example.FinalProject.models.accounts.SavingAccount;
import com.example.FinalProject.models.users.AccountHolder;
import com.example.FinalProject.models.users.Address;
import com.example.FinalProject.repositories.accounts.CheckingAccountRepository;
import com.example.FinalProject.repositories.accounts.SavingAccountRepository;
import com.example.FinalProject.repositories.users.AccountHolderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountHolderTests {

    //WebApplicationContext: lo necesita MockMvc para realizar las peticiones
    @Autowired
    WebApplicationContext context;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    SavingAccountRepository savingAccountRepository;


    //MockMvc: Nos sirve para falsear peticiones http
    private MockMvc mockMvc;

    //ObjectMapper: Convierte objetos a formato json
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        //Construimos el falseador, introduciendo el contexto de la app
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper.findAndRegisterModules();

    }
    @AfterEach
    public void clean(){
        accountHolderRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        savingAccountRepository.deleteAll();
    }

    @Test
    void shouldAddAccountHolder() throws Exception {

        Address address = new Address("Calle 1", "Barcelona", "08019", "España");
        AccountHolder accountHolder = new AccountHolder("User", "123456", "Manuel", LocalDate.of(1985, 02, 17), address);
        //Convertimos el objeto a formato json
        String body = objectMapper.writeValueAsString(accountHolder);

        MvcResult result = mockMvc.perform(post("/account-holder/create-user").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Manuel"));
    }

    @Test
    @WithMockUser(username = "User", password = "123456")
    void shouldMakeTransaction() throws Exception {

        Address address = new Address("Calle 1", "Barcelona", "08019", "España");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("User", "123456", "Manuel", LocalDate.of(1985, 02, 17), address));
        CheckingAccount checkingAccount = checkingAccountRepository.save(new CheckingAccount(BigDecimal.valueOf(1000), accountHolder, "SKCHECKING"));

        Address address2 = new Address("Calle 2", "Madrid", "45687", "España");
        AccountHolder accountHolder2 = accountHolderRepository.save(new AccountHolder("User2", "123456", "David", LocalDate.of(2000, 02, 17), address2));
        SavingAccount savingAccount = savingAccountRepository.save(new SavingAccount(BigDecimal.valueOf(2500), accountHolder2, "SKSAVING"));

        TransactionDTO transaction = new TransactionDTO(BigDecimal.valueOf(400), checkingAccount.getId(), "David", savingAccount.getId());

        //Convertimos el objeto a formato json
        String body = objectMapper.writeValueAsString(transaction);

        MvcResult result = mockMvc.perform(post("/account-holder/transference")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("SKSAVING"));
    }


    @Test
    @WithMockUser(username = "User", password = "123456")
    public void getMyAccounts() throws Exception {

        Address address = new Address("Calle 1", "Barcelona", "08019", "España");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("User", "123456", "Manuel", LocalDate.of(1985, 02, 17), address));
        CheckingAccount checkingAccount = checkingAccountRepository.save(new CheckingAccount(BigDecimal.valueOf(1000), accountHolder, "SKCHECKING"));


        MvcResult result = mockMvc.perform(get("/account-holder/my-accounts")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();


        assertTrue(result.getResponse().getContentAsString().contains("SKCHECKING"));

    }

    @Test
    @WithMockUser(username = "User", password = "123456")
    public void getMyBalance() throws Exception {

        Address address = new Address("Calle 1", "Barcelona", "08019", "España");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("User", "123456", "Manuel", LocalDate.of(1985, 02, 17), address));
        CheckingAccount checkingAccount = checkingAccountRepository.save(new CheckingAccount(BigDecimal.valueOf(1000), accountHolder, "SKCHECKING"));


        MvcResult result = mockMvc.perform(get("/account-holder/my-balance")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();


        assertTrue(result.getResponse().getContentAsString().contains(BigDecimal.valueOf(1000).toString()));

    }


}
