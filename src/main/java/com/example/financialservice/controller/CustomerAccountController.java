package com.example.financialservice.controller;

import com.example.financialservice.dto.AccountDto;
import com.example.financialservice.dto.CustomerDto;
import com.example.financialservice.service.CustomerAccountService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerAccountController {

    @Autowired
    private CustomerAccountService customerAccountService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerAccountController.class);

    @GetMapping()
    @Operation(description = "Get the customer information based on id")
    public ResponseEntity<CustomerDto> getCustomerInfoController(@RequestParam(value = "id") Long id) {
        logger.info("Getting the user information for user with id {}", id);

        CustomerDto resultCustomerResponse = customerAccountService.getCustomerInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body(resultCustomerResponse);
    }

    @PostMapping("/account")
    @Operation(description = "Create a new account for existing customer based on customerId and initial credit")
    public ResponseEntity<AccountDto> createCustomerAccountController(
            @RequestParam(value = "customerId") Long customerId,
            @RequestParam(value = "initialCredit") Integer initialCredit) {

        AccountDto newAccountResponse = customerAccountService.createNewAccount(customerId, initialCredit);
        return new ResponseEntity<>(newAccountResponse, HttpStatus.CREATED);
    }
}
