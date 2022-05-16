package com.example.financialservice.service;

import com.example.financialservice.database.entity.Account;
import com.example.financialservice.database.entity.Customer;
import com.example.financialservice.database.entity.Transactions;
import com.example.financialservice.database.repository.AccountRepository;
import com.example.financialservice.database.repository.CustomerRepository;
import com.example.financialservice.database.repository.TransactionsRepository;
import com.example.financialservice.dto.AccountDto;
import com.example.financialservice.dto.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class CustomerAccountServiceImplTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private TransactionsRepository transactionsRepository;

    @InjectMocks
    private CustomerAccountServiceImpl customerAccountServiceImpl;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewAccountSuccessfullyWithInitialCreditNot0() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setBalance(100);
        customer.setName("John");
        customer.setSurname("Smith");

        Account newAccount = new Account();
        newAccount.setId(100L);
        newAccount.setCustomer(customer);

        Transactions transactions = new Transactions();
        transactions.setTransactions(50);
        List<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(transactions);

        Mockito.when(customerRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(customer));
        Mockito.when(accountRepository.save(ArgumentMatchers.any())).thenReturn(newAccount);
        Mockito.when(transactionsRepository.save(ArgumentMatchers.any())).thenReturn(transactions);
        Mockito.when(transactionsRepository.findByAccountId(ArgumentMatchers.any())).thenReturn(transactionsList);

        AccountDto result = customerAccountServiceImpl.createNewAccount(1L, 50);
        assertEquals(100L, result.getAccountId());
        assertEquals(50, result.getTransactionAmounts().get(0));
    }

    @Test
    void createNewAccountSuccessfullyWith0InitialCredit() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setBalance(100);
        customer.setName("John");
        customer.setSurname("Smith");

        Account newAccount = new Account();
        newAccount.setId(100L);
        newAccount.setCustomer(customer);

        Mockito.when(customerRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(customer));
        Mockito.when(accountRepository.save(ArgumentMatchers.any())).thenReturn(newAccount);

        AccountDto result = customerAccountServiceImpl.createNewAccount(1L, 0);
        assertEquals(100L, result.getAccountId());
        assertEquals(Collections.emptyList(), result.getTransactionAmounts());
    }

    @Test
    void createNewAccountWithInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> customerAccountServiceImpl.createNewAccount(1000L, 0));
    }

    @Test
    void getCustomerInfoSuccessfully() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setBalance(50);
        customer.setName("John");
        customer.setSurname("Smith");

        Account account1 = new Account();
        account1.setId(100L);
        account1.setCustomer(customer);

        Account account2 = new Account();
        account2.setId(101L);
        account2.setCustomer(customer);

        List<Account> accountList = new ArrayList<>();
        accountList.add(account1);
        accountList.add(account2);

        Transactions transactions = new Transactions();
        transactions.setTransactions(70);
        transactions.setAccount(account1);
        Transactions transactions2 = new Transactions();
        transactions2.setTransactions(80);
        transactions2.setAccount(account1);
        List<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(transactions);
        transactionsList.add(transactions2);

        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Mockito.when(accountRepository.findByCustomerId(1L)).thenReturn(accountList);
        Mockito.when(transactionsRepository.findByAccountId(100L)).thenReturn(transactionsList);

        CustomerDto result = customerAccountServiceImpl.getCustomerInfo(1L);
        assertEquals(1L, result.getCustomerId());
        assertEquals(50, result.getBalance());
        assertEquals("John", result.getName());
        assertEquals("Smith", result.getSurname());
        assertEquals(2, result.getAccounts().size());

        //check transaction amounts for different accounts
        assertEquals(2, result.getAccounts().get(0).getTransactionAmounts().size());
        assertEquals(70, result.getAccounts().get(0).getTransactionAmounts().get(0));
        assertEquals(80, result.getAccounts().get(0).getTransactionAmounts().get(1));

        assertEquals(101L, result.getAccounts().get(1).getAccountId());
        assertEquals(Collections.emptyList(), result.getAccounts().get(1).getTransactionAmounts());

    }

    @Test
    void getCustomerInfoWithInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> customerAccountServiceImpl.getCustomerInfo(1000L));
    }
}
