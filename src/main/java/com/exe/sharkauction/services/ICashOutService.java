package com.exe.sharkauction.services;

import com.exe.sharkauction.models.CashOutEntity;

import java.util.List;

public interface ICashOutService {
    CashOutEntity createCashOut(CashOutEntity cashOut);


    void completedCashOut(long id);

    List<CashOutEntity> myCashOut();

    List<CashOutEntity> getAllCashOut();
}
