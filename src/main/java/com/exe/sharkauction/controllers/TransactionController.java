package com.exe.sharkauction.controllers;

import com.exe.sharkauction.models.SystemTransactionEntity;
import com.exe.sharkauction.services.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionService systemTransactionService;

    @GetMapping("/user")
    public List<SystemTransactionEntity> getTransactionsByUserId() {
        return systemTransactionService.getTransactionsByUserId();
    }
    @GetMapping("")
    public List<SystemTransactionEntity> getTransactions() {
        return systemTransactionService.getTransactionsList();
    }

}
