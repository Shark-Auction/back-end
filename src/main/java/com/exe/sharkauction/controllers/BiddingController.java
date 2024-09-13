package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.BiddingEntity;
import com.exe.sharkauction.requests.BidRequest;
import com.exe.sharkauction.services.IBiddingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/bidding")
@RequiredArgsConstructor
public class BiddingController {

    private final IBiddingService biddingService;
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public CoreApiResponse<BiddingEntity> createBidding(
            @Valid @RequestBody BidRequest bidRequest
    ){
        biddingService.createBidding(bidRequest);
        return CoreApiResponse.success("Bidding successfully");
    }

    @GetMapping("auction/{id}")
    public CoreApiResponse<List<BiddingEntity>> getAllBiddingForAuction(@Valid @PathVariable Long id){
        List<BiddingEntity> material = biddingService.getBiddingByAuctionId(id);
        return CoreApiResponse.success(material);
    }
}