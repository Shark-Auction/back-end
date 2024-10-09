package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.securities.UserPrincipal;
import com.exe.sharkauction.models.CashOutEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.models.WalletEntity;
import com.exe.sharkauction.models.enums.CashOutStatus;
import com.exe.sharkauction.repositories.ICashOutRepository;
import com.exe.sharkauction.repositories.IWalletRepository;
import com.exe.sharkauction.services.ICashOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CashOutService implements ICashOutService {
    private final ICashOutRepository cashOutRepository;
    private final IWalletRepository walletRepository;
    @Override
    public CashOutEntity createCashOut(CashOutEntity cashOut){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        cashOut.setUser(user);
        cashOut.setStatus(CashOutStatus.Pending);
        WalletEntity wallet = walletRepository.findByUser(user);
        cashOut.setMoney(wallet.getMoney());
        wallet.setMoney(0f);
        walletRepository.save(wallet);
        return cashOutRepository.save(cashOut);
    }
    @Override
    public void completedCashOut(long id){
        CashOutEntity cashOut = cashOutRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Không tìm lấy lệnh rút tiền"));
        cashOut.setStatus(CashOutStatus.Completed);
        cashOutRepository.save(cashOut);
    }

    @Override
    public List<CashOutEntity> myCashOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        return cashOutRepository.findByUser(user);
    }

    @Override
    public List<CashOutEntity> getAllCashOut() {
        return cashOutRepository.findAll();
    }


}
