package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.securities.UserPrincipal;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.models.WalletEntity;
import com.exe.sharkauction.repositories.IWalletRepository;
import com.exe.sharkauction.services.IWalletService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class WalletService implements IWalletService {
    private final IWalletRepository walletRepository;

    @Override
    public WalletEntity myWallet(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return walletRepository.findByUser(user);
    }
}
