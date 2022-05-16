package com.example.financialservice.service;

import com.example.financialservice.dto.AccountDto;
import com.example.financialservice.dto.CustomerDto;

public interface CustomerAccountService {
    CustomerDto getCustomerInfo(Long id);

    AccountDto createNewAccount(Long id, Integer initialCredit);
}
