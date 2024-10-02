package com.exe.sharkauction.services;

import com.exe.sharkauction.models.VoucherEntity;

import java.util.List;

public interface IVoucherService {
    VoucherEntity createVoucher(VoucherEntity voucher);

    VoucherEntity getVoucher(long id);

    List<VoucherEntity> getAll();

    void deleteVoucher(long id);
}
