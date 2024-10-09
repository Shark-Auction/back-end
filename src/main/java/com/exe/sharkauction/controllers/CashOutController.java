package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.CashOutEntity;
import com.exe.sharkauction.requests.CashOutRequest;
import com.exe.sharkauction.services.ICashOutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.exe.sharkauction.mappers.ICashOutMapper.INSTANCE;


@RestController
@RequestMapping("${app.api.version.v1}/cashouts")
@RequiredArgsConstructor
public class CashOutController {
    private final ICashOutService cashOutService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public CoreApiResponse<CashOutEntity> createCashOut(
            @Valid @RequestBody CashOutRequest cashOutRequest
    ){
        CashOutEntity cashOut = cashOutService.createCashOut(INSTANCE.toModel(cashOutRequest));
        return CoreApiResponse.success(cashOut,"Tạo yêu cầu rút tiền thành công");
    }

    @GetMapping("")
    public CoreApiResponse<List<CashOutEntity>> listCashOut(){
        return CoreApiResponse.success(cashOutService.getAllCashOut());
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cash")
    public CoreApiResponse<List<CashOutEntity>> myCashOut(){
        return CoreApiResponse.success(cashOutService.myCashOut());
    }

    @PutMapping("/completed/{id}")
    public CoreApiResponse<?> completeCashOut( @PathVariable Long id){
        cashOutService.completedCashOut(id);
        return CoreApiResponse.success("Hoàn thành yêu cầu rút tiền");

    }

}
