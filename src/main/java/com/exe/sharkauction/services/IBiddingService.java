package com.exe.sharkauction.services;

import com.exe.sharkauction.models.BiddingEntity;
import com.exe.sharkauction.requests.BidRequest;

import java.util.List;

public interface IBiddingService {
    void createBidding(BidRequest bidRequest) ;
    List<BiddingEntity> getBiddingByAuctionId(long id);
}
