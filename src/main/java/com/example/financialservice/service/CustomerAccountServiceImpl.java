package com.example.financialservice.service;

import com.example.financialservice.database.entity.Account;
import com.example.financialservice.database.entity.Transactions;
import com.example.financialservice.dto.AccountDto;
import com.example.financialservice.dto.CustomerDto;
import com.example.financialservice.database.entity.Customer;
import com.example.financialservice.database.repository.AccountRepository;
import com.example.financialservice.database.repository.CustomerRepository;
import com.example.financialservice.database.repository.TransactionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerAccountServiceImpl implements CustomerAccountService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomerAccountServiceImpl.class);

    @Override
    public AccountDto createNewAccount(Long id, Integer initialCredit) {

        Optional<Customer> customerFromDB = customerRepository.findById(id);
        AccountDto accountDto = new AccountDto();

        if (customerFromDB.isPresent()) {
            Account newAccount = new Account();
            newAccount.setCustomer(customerFromDB.get());
            Account newAccountInDB = accountRepository.save(newAccount);
            accountDto.setAccountId(newAccountInDB.getId());
            logger.info("New account is created with id {}", newAccount.getId());

            if (initialCredit != 0) {
                addTransactionToAccount(newAccountInDB, initialCredit);
                accountDto.setTransactionAmounts(getTransactionsForAccount(newAccountInDB.getId()));
                return accountDto;
            } else {
                accountDto.setTransactionAmounts(Collections.emptyList());
                return accountDto;
            }
        } else {
            throw new EntityNotFoundException("User with id: " + id + " cannot be found");
        }
    }

    @Override
    public CustomerDto getCustomerInfo(Long id) {

        Optional<Customer> customerInfoFromDB = customerRepository.findById(id);
        List<Account> accountsFromDB = accountRepository.findByCustomerId(id);

        if (customerInfoFromDB.isPresent()) {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setCustomerId(customerInfoFromDB.get().getId());
            customerDto.setName(customerInfoFromDB.get().getName());
            customerDto.setSurname(customerInfoFromDB.get().getSurname());
            customerDto.setBalance(customerInfoFromDB.get().getBalance());

            List<AccountDto> listOfAccountsForSameUser = new ArrayList<>();
            for (Account accountInDB : accountsFromDB) {
                listOfAccountsForSameUser.add(new AccountDto(accountInDB.getId(), getTransactionsForAccount(accountInDB.getId())));
            }

            customerDto.setAccounts(listOfAccountsForSameUser);

            logger.info("the user has id: {}, name: {}, surname: {}", id, customerInfoFromDB.get().getName(),
                    customerInfoFromDB.get().getSurname());

            return customerDto;
        } else {
            throw new EntityNotFoundException("User with id: " + id + " cannot be found");
        }
    }

    private List<Integer> getTransactionsForAccount(Long accountId) {
        List<Integer> transactionsForSameAccountResultList = new ArrayList<>();
        List<Transactions> transactionsForSameAccountFromDB = transactionsRepository.findByAccountId(accountId);

        if (!transactionsForSameAccountFromDB.isEmpty()) {
            for (Transactions transaction : transactionsForSameAccountFromDB) {
                transactionsForSameAccountResultList.add(transaction.getTransactions());
            }
        }
        return transactionsForSameAccountResultList;
    }

    private void addTransactionToAccount(Account account, Integer transactionAmount) {
        Transactions transactionForNewlyOpenedAccount = new Transactions();
        transactionForNewlyOpenedAccount.setAccount(account);
        transactionForNewlyOpenedAccount.setTransactions(transactionAmount);
        transactionsRepository.save(transactionForNewlyOpenedAccount);
    }
}
