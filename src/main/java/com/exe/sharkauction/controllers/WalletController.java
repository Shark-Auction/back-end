package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.WalletEntity;
import com.exe.sharkauction.services.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${app.api.version.v1}/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final IWalletService walletService;
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mywallet")
    public CoreApiResponse<WalletEntity>getMyViolate(){
        WalletEntity myWallet = walletService.myWallet();
        return CoreApiResponse.success(myWallet);
    }
}
