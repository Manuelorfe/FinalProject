package com.example.FinalProject;

import com.example.FinalProject.dtos.ThirdPartyTransactionDTO;
import com.example.FinalProject.models.accounts.CheckingAccount;
import com.example.FinalProject.models.users.AccountHolder;
import com.example.FinalProject.models.users.Address;
import com.example.FinalProject.models.users.ThirdPartyUser;
import com.example.FinalProject.repositories.accounts.CheckingAccountRepository;
import com.example.FinalProject.repositories.users.AccountHolderRepository;
import com.example.FinalProject.repositories.users.ThirdPartyUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ThirdPartyTest {

    //WebApplicationContext: lo necesita MockMvc para realizar las peticiones
    @Autowired
    WebApplicationContext context;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    ThirdPartyUserRepository thirdPartyUserRepository;

    //MockMvc: Nos sirve para falsear peticiones http
    MockMvc mockMvc;

    //ObjectMapper: Convierte objetos a formato json
    final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        //Construimos el falseador, introduciendo el contexto de la app
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    void shouldMakeThirdPArtyTransaction() throws Exception {

        ThirdPartyUser thirdPartyUser = thirdPartyUserRepository.save(new ThirdPartyUser("ThirdPartyUser", "654321", "Jose", "HK2345"));

        Address address = new Address("Calle 1", "Barcelona", "08019", "España");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("User", "123456", "Manuel", LocalDate.of(1985, 02, 17), address));
        CheckingAccount checkingAccount = checkingAccountRepository.save(new CheckingAccount(BigDecimal.valueOf(1000), accountHolder, "SKCHECKING"));

        ThirdPartyTransactionDTO thirdPartyTransactionDTO = new ThirdPartyTransactionDTO(thirdPartyUser.getId(),BigDecimal.valueOf(500), checkingAccount.getId(),checkingAccount.getSecretKey());

        //Convertimos el objeto a formato json
        String body = objectMapper.writeValueAsString(thirdPartyTransactionDTO);

        MvcResult result = mockMvc.perform(post("/third-party/transference")
                        .header("hashedKey", "HK2345")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("SKCHECKING"));
    }

}
