package com.example.FinalProject;

import com.example.FinalProject.models.users.AccountHolder;
import com.example.FinalProject.models.users.Address;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountHolderTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //objectMapper.findAndRegisterModules();
    }


    @Test
    void create_user() throws Exception {

        Address address = new Address("Calle 1", "Barcelona", "08019", "España");
        AccountHolder accountHolder = new AccountHolder("User", "123456", "Manuel", LocalDate.of(1985, 02, 17),address);
        String body = objectMapper.writeValueAsString(accountHolder);
        MvcResult result = mockMvc.perform(post("/account-holder/create-patient")
                .content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Manuel"));
    }






}
