package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.models.ViolateEntity;
import com.exe.sharkauction.requests.AuctionRequest;
import com.exe.sharkauction.requests.ViolateRequest;
import com.exe.sharkauction.services.IAuctionService;
import com.exe.sharkauction.services.IViolateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.exe.sharkauction.mappers.IViolateMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/violate")
@RequiredArgsConstructor
public class ViolateController {
    private final IViolateService violateService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public CoreApiResponse<ViolateEntity> createAuction(
            @Valid @RequestBody ViolateRequest request
    ){
        ViolateEntity violate = violateService.createViolate(INSTANCE.toModel(request));
        return CoreApiResponse.success(violate,"Insert auction successfully");
    }

//    @GetMapping("")
//    public CoreApiResponse<List<AuctionEntity>> getAllAuctions(){
//        List<AuctionEntity> auctions = violateService.getAllViolate();
//        return CoreApiResponse.success(auctions);
//    }
//
//    @GetMapping("/{id}")
//    public CoreApiResponse<AuctionEntity> getAuctionById(@Valid @PathVariable Long id){
//        AuctionEntity auction = violateService.getAuctionById(id);
//        return CoreApiResponse.success(auction);
//    }
}
