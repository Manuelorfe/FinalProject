package com.example.FinalProject;

import com.example.FinalProject.dtos.CheckingStudentDTO;
import com.example.FinalProject.models.accounts.CreditCard;
import com.example.FinalProject.models.accounts.SavingAccount;
import com.example.FinalProject.models.users.AccountHolder;
import com.example.FinalProject.models.users.Address;
import com.example.FinalProject.models.users.Admin;
import com.example.FinalProject.models.users.ThirdPartyUser;
import com.example.FinalProject.repositories.accounts.CheckingAccountRepository;
import com.example.FinalProject.repositories.accounts.CreditCardRepository;
import com.example.FinalProject.repositories.accounts.SavingAccountRepository;
import com.example.FinalProject.repositories.users.AccountHolderRepository;
import com.example.FinalProject.repositories.users.AdminRepository;
import com.example.FinalProject.repositories.users.ThirdPartyUserRepository;
import com.example.FinalProject.repositories.users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AdminTests {

    //WebApplicationContext: lo necesita MockMvc para realizar las peticiones
    @Autowired
    WebApplicationContext context;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ThirdPartyUserRepository thirdPartyUserRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    SavingAccountRepository savingAccountRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    AdminRepository adminRepository;

    //MockMvc: Nos sirve para falsear peticiones http
    MockMvc mockMvc;

    //ObjectMapper: Convierte objetos a formato json
    final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        //Construimos el falseador, introduciendo el contexto de la app
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        savingAccountRepository.deleteAll();
        creditCardRepository.deleteAll();
        thirdPartyUserRepository.deleteAll();

    }

    @Test
    void shouldAddThirdPartyUser() throws Exception {

        ThirdPartyUser thirdPartyUser = new ThirdPartyUser("ThirdPartyUser", "123456", "Jose", "HK001");
        //Convertimos el objeto a formato json
        String body = objectMapper.writeValueAsString(thirdPartyUser);

        MvcResult result = mockMvc.perform(post("/admin/add-third-party-user")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Jose"));
    }

    @Test
    void shouldCreateBankAccount() throws Exception {

        Address address = new Address("Calle 1", "Barcelona", "08019", "España");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("User", "123456", "Manuel", LocalDate.of(1985, 02, 17), address));

        CheckingStudentDTO checkingStudentDTO = new CheckingStudentDTO
                (BigDecimal.valueOf(400), "SK0001", accountHolder.getId(), LocalDate.now());

        //Convertimos el objeto a formato json
        String body = objectMapper.writeValueAsString(checkingStudentDTO);

        MvcResult result = mockMvc.perform(post("/admin/create-bank-account")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("User"));
    }

    @Test
    void shouldCreateSavingAccount() throws Exception {

        Address address2 = new Address("Calle 2", "Madrid", "45687", "España");
        AccountHolder accountHolder2 = accountHolderRepository.save(new AccountHolder("User2", "123456", "David", LocalDate.of(2000, 02, 17), address2));
        SavingAccount savingAccount = new SavingAccount(BigDecimal.valueOf(2500), accountHolder2, "SKSAVING");

        //Convertimos el objeto a formato json
        String body = objectMapper.writeValueAsString(savingAccount);

        MvcResult result = mockMvc.perform(post("/admin/create-saving-account")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("SKSAVING"));
    }

    @Test
    void shouldCreateCreditCard() throws Exception {

        Address address = new Address("Calle", "Madrid", "45687", "España");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("User", "123456", "David", LocalDate.of(2000, 02, 17), address));
        CreditCard creditCard = new CreditCard(BigDecimal.valueOf(500), accountHolder);
        //Convertimos el objeto a formato json
        String body = objectMapper.writeValueAsString(creditCard);

        MvcResult result = mockMvc.perform(post("/admin/create-credit-card")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("User"));
    }

    @Test
    public void deleteUser() throws Exception {
        Admin admin = adminRepository.save(new Admin("AdminUser", "123456", "Manu"));

        //Porque por defecto me crea el Admin
        mockMvc.perform(delete("/admin/remove-user/{id}", admin.getId()))
                .andDo(print())
                .andExpect(status().isAccepted());

        assertEquals(0, userRepository.findAll().size());
    }

    @Test
    public void getAllUsers() throws Exception {

        Admin admin = adminRepository.save(new Admin("AdminUser", "123456", "Manu"));


        MvcResult result = mockMvc.perform(get("/admin/get-users")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(admin.getUsername()));
    }

    @Test
    public void patchMailingAddress() throws Exception {

        Address address = new Address("Calle", "Barcelona", "45687", "España");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("User", "123456", "David", LocalDate.of(2000, 02, 17), address));
        Address mailingAdress = new Address("MailingStreet", "Madrid", "12345", "España");

        String body = objectMapper.writeValueAsString(mailingAdress);

        MvcResult result = mockMvc.perform(patch("/admin/add-mailing-adress-ah/{id}",accountHolder.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("MailingStreet"));

    }

    @Test
    public void patchChangeBalance() throws Exception {

        Address address = new Address("Calle", "Barcelona", "45687", "España");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("User", "123456", "David", LocalDate.of(2000, 02, 17), address));
        CreditCard creditCard = creditCardRepository.save(new CreditCard(BigDecimal.valueOf(500), accountHolder));

        BigDecimal amount = BigDecimal.valueOf(5000);


        MvcResult result = mockMvc.perform(patch("/admin/change-balance/{id}",creditCard.getId())
                        .param("amount", amount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(BigDecimal.valueOf(5000).toString()));

    }

    @Test
    void shouldUpdateAccountHolder() throws Exception {

        Address address2 = new Address("Calle 2", "Madrid", "45687", "España");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("User2", "123456", "David", LocalDate.of(2000, 02, 17), address2));

        AccountHolder accountHolder2 = accountHolderRepository.save(new AccountHolder("NuevoUser", "654321", "Pepe", LocalDate.of(1500, 02, 17), address2));
        //Convertimos el objeto a formato json
        String body = objectMapper.writeValueAsString(accountHolder2);

        MvcResult result = mockMvc.perform(put("/admin/update-ah/{id}",accountHolder.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("NuevoUser"));
    }




}
