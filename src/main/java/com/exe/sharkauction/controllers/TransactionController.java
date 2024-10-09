package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.SystemTransactionEntity;
import com.exe.sharkauction.services.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionService systemTransactionService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public CoreApiResponse<List<SystemTransactionEntity>> getTransactionsByUserId() {
        return CoreApiResponse.success(systemTransactionService.getTransactionsByUserId());
    }
    @GetMapping("")
    public CoreApiResponse<List<SystemTransactionEntity>> getTransactions() {
        return CoreApiResponse.success(systemTransactionService.getTransactionsList());
    }

}
