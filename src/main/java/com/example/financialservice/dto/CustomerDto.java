package com.example.financialservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerDto {
    private Long customerId;
    private String name;
    private String surname;
    private Integer balance;
    private List<AccountDto> accounts;
}
