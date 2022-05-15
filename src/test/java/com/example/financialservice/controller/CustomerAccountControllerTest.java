package com.example.financialservice.controller;

import com.example.financialservice.dto.AccountDto;
import com.example.financialservice.dto.CustomerDto;
import com.example.financialservice.service.CustomerAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerAccountController.class)
class CustomerAccountControllerTest {

    @MockBean
    CustomerAccountService customerAccountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void verifyGetUserInfoSuccessfully() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(1L);
        customerDto.setAccounts(Collections.emptyList());
        customerDto.setBalance(100);
        customerDto.setName("Alex");
        customerDto.setSurname("John");

        Mockito.when(customerAccountService.getCustomerInfo(1L)).thenReturn(customerDto);

        mockMvc.perform(get("/customer")
                        .param("id", String.valueOf(1))
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Alex"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accounts").exists());
    }

    @Test
    void getUserWithInvalidUserId() throws Exception {
        Mockito.when(customerAccountService.getCustomerInfo(1000L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(
                        get("/customer")
                                .param("id", String.valueOf(1000))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void createNewAccountSuccessfully() throws Exception {

        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(100L);
        accountDto.setTransactionAmounts(List.of(1,2,3,4,5));

        Mockito.when(customerAccountService.createNewAccount(ArgumentMatchers.any(), eq(100))).thenReturn(accountDto);

        mockMvc.perform(post("/customer/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("customerId", String.valueOf(1))
                                .param("initialCredit", String.valueOf(100))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(100))
                .andExpect(jsonPath("$.transactionAmounts").exists());
    }

    @Test
    void createNewAccountForInvalidUser() throws Exception {

        Mockito.when(customerAccountService.createNewAccount(ArgumentMatchers.any(), ArgumentMatchers.any())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post("/customer/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("customerId", String.valueOf(1))
                        .param("initialCredit", String.valueOf(100))
                )
                .andExpect(status().isNotFound());
    }
}
