package com.lala.jwt.service;

import com.lala.jwt.entity.Customer;
import com.lala.jwt.entity.Transaction;
import com.lala.jwt.repository.CustomerRepository;
import com.lala.jwt.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankService {

    @Autowired
    private  CustomerRepository customerRepository;
    @Autowired
    private  TransactionRepository transactionRepository;

    //@PreAuthorize("hasRole('USER')")
    public Customer getAccount(){
        String email = getLoggedInUserEmail();
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No account found for user: " + email));

    }

    @PreAuthorize("hasRole('USER')")
    public String getBalance(){
        String email = getLoggedInUserEmail();
        return customerRepository.findByEmail(email)
                .map(customer -> "Balance for " + customer.getEmail() + ": " + customer.getBalance() + " INR")
                .orElse("No account found for user: " + email);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminReport(){
        return "Admin Report: \n" +
                "Total Customers: " + customerRepository.count() + "\n" +
                "Total Transactions: " + transactionRepository.count() + "\n" +
                "Total Balance: " + customerRepository.findAll().stream()
                        .mapToDouble(customer -> customer.getBalance())
                        .sum() + " INR";
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional
    public Transaction withdraw(double amount) {
        String email = getLoggedInUserEmail();
        return customerRepository.findByEmail(email)
                .map(customer -> {
                    if (customer.getBalance() >= amount) {
                        customer.setBalance(customer.getBalance() - amount);
                        customerRepository.save(customer);
                        Transaction transaction = new Transaction();
                        transaction.setEmail(email);
                        transaction.setType("WITHDRAWAL");
                        transaction.setTimestamp(LocalDateTime.now());
                        transactionRepository.save(transaction);
                        return transaction;
                    } else {
                        throw new IllegalArgumentException("Insufficient balance for withdrawal.");
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("No account found for user: " + email));
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional
    public Transaction deposit(double amount) {
        String email = getLoggedInUserEmail();
        return customerRepository.findByEmail(email)
                .map(customer -> {
                    customer.setBalance(customer.getBalance() + amount);
                    customerRepository.save(customer);
                    Transaction transaction = new Transaction();
                    transaction.setEmail(email);
                    transaction.setAmount(amount);
                    transaction.setType("DEPOSIT");
                    transaction.setTimestamp(LocalDateTime.now());
                    transactionRepository.save(transaction);
                    return transaction;
                })
                .orElseThrow(() -> new IllegalArgumentException("No account found for user: " + email));
    }
    //User should be able to see his transaction history only not others
    //Admin should be able to see all transaction history
    @PostAuthorize("returnObject.isEmpty() or returnObject[0].email == authentication.name or hasRole('ADMIN')")
    public List<Transaction> getTransactionHistory(String email) {
        return transactionRepository.findByEmail(email);
    }


   //Authentication Object is a special Object in spring that represents the currently authenticated user eg: there username,roles,principal
   //It is stored in the SecurityContextHolder, which is a thread-local storage for security-related information
    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
