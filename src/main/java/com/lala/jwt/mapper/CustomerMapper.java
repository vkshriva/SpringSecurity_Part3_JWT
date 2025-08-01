package com.lala.jwt.mapper;

import com.lala.jwt.entity.Customer;
import com.lala.jwt.model.RegisterRequest;

public class CustomerMapper {

    public static RegisterRequest toModel(Customer entity) {
        if (entity == null) return null;
        RegisterRequest model = new RegisterRequest();
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPwd());
        model.setRole(entity.getRole());
        model.setBalance(entity.getBalance());
        return model;
    }

    public static Customer toEntity(RegisterRequest model) {
        if (model == null) return null;
        Customer entity = new Customer();
        entity.setEmail(model.getEmail());
        entity.setPwd(model.getPassword());
        entity.setRole(model.getRole());
        entity.setBalance(model.getBalance());
        return entity;
    }
}