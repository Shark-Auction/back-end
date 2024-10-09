package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.securities.UserPrincipal;
import com.exe.sharkauction.models.SystemTransactionEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.repositories.ISystemTransactionRepository;
import com.exe.sharkauction.services.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TransactionService implements ITransactionService {
    private final ISystemTransactionRepository transactionRepository;

    @Override
    public List<SystemTransactionEntity> getTransactionsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return transactionRepository.findTransactionsByUser(user);
    }
    @Override
    public List<SystemTransactionEntity> getTransactionsList() {
        return transactionRepository.findAll();
    }


}
