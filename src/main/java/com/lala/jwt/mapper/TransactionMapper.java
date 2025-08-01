package com.lala.jwt.mapper;

import com.lala.jwt.entity.Transaction;
import com.lala.jwt.entity.Customer;
import com.lala.jwt.model.TransactionModel;

public class TransactionMapper {

    public static TransactionModel toModel(Transaction entity) {
        if (entity == null) return null;
        TransactionModel model = new TransactionModel();
        model.setId(entity.getId());
        model.setEmail(entity.getEmail());
        model.setAmount(entity.getAmount());
        model.setType(entity.getType());
        model.setTimestamp(entity.getTimestamp());
        return model;
    }

    public static Transaction toEntity(TransactionModel model, Customer customer) {
        if (model == null || customer == null) return null;
        Transaction entity = new Transaction();
        entity.setId(model.getId());
        entity.setEmail(model.getEmail());
        entity.setAmount(model.getAmount());
        entity.setType(model.getType());
        entity.setTimestamp(model.getTimestamp());
        return entity;
    }
}
