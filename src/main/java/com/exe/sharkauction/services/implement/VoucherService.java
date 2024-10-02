package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.exceptions.DataNotFoundException;
import com.exe.sharkauction.components.securities.UserPrincipal;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.models.VoucherEntity;
import com.exe.sharkauction.models.enums.VoucherStatus;
import com.exe.sharkauction.repositories.IVoucherRepository;
import com.exe.sharkauction.services.IVoucherService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class VoucherService implements IVoucherService {

    private final IVoucherRepository voucherRepository;

    @Override
    public VoucherEntity createVoucher(VoucherEntity voucher){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        voucher.setCreator(user);
        voucher.setStatus(VoucherStatus.Pending);

        return voucherRepository.save(voucher);

    }
    @Override
    public VoucherEntity getVoucher(long id){
        return voucherRepository.findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("Voucher", "id", id));
    }
    @Override
    public List<VoucherEntity> getAll(){
        return voucherRepository.findAll();
    }
    @Override
    public void deleteVoucher(long id){
        VoucherEntity voucher = voucherRepository.findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("Voucher", "id", id));
        voucherRepository.delete(voucher);
    }

}
