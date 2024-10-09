package com.exe.sharkauction.services;

import com.exe.sharkauction.models.SystemTransactionEntity;

import java.util.List;

public interface ITransactionService {
    List<SystemTransactionEntity> getTransactionsByUserId();

    List<SystemTransactionEntity> getTransactionsList();
}
