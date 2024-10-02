package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.VoucherEntity;
import com.exe.sharkauction.requests.VoucherRequest;
import com.exe.sharkauction.services.IVoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.exe.sharkauction.mappers.IVoucherMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/voucher")
@RequiredArgsConstructor
public class VoucherController {
    private final IVoucherService voucherService;

    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @PostMapping("")
    public CoreApiResponse<VoucherEntity> createVoucher(
            @Valid @RequestBody VoucherRequest request
    ){
        VoucherEntity voucher = voucherService.createVoucher(INSTANCE.toModel(request));
        return CoreApiResponse.success(voucher,"Thêm mới voucher thành công");
    }

    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @GetMapping("")
    public CoreApiResponse<List<VoucherEntity>> getAllVoucher(){
        List<VoucherEntity> vouchers = voucherService.getAll();
        return CoreApiResponse.success(vouchers);
    }
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @GetMapping("/{id}")
    public CoreApiResponse<VoucherEntity> getVoucherById(@Valid @PathVariable Long id){
        VoucherEntity voucher = voucherService.getVoucher(id);
        return CoreApiResponse.success(voucher);
    }

    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return CoreApiResponse.success("Xoá voucher thành công") ;
    }
}
