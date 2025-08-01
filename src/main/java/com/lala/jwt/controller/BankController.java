package com.lala.jwt.controller;

import com.lala.jwt.entity.Customer;
import com.lala.jwt.entity.Transaction;
import com.lala.jwt.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BankController {

    @Autowired
    private BankService bankService;

     @GetMapping("/account")
     public ResponseEntity<Customer> getAccountDetails() {
         Customer customer  = bankService.getAccount();
            return ResponseEntity.ok(customer);
     }


}
