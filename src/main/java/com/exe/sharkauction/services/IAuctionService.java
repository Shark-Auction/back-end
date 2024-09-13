package com.exe.sharkauction.services;

import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.models.enums.AuctionStatus;
import com.exe.sharkauction.models.enums.ProductCondition;
import com.exe.sharkauction.requests.UpdateTimeAuctionRequest;
import com.exe.sharkauction.responses.AuctionResponse;
import com.exe.sharkauction.responses.ListBidForAuction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAuctionService {
    AuctionEntity createAuction(AuctionEntity auction) ;
    AuctionEntity getAuctionById(long id);
    List<AuctionEntity> getAllAuctions();

    List<AuctionEntity> getAuctionsByCategoryId(Long categoryId);

    List<AuctionEntity> getAuctionsByBrandId(Long brandId);
    List<AuctionEntity> getMyAuctionsByStatus(AuctionStatus status);

    Page<AuctionEntity> searchAuctions(Long collectionId, Long categoryId,
                                       Float minPrice, Float maxPrice, Long brandId,
                                       ProductCondition condition, AuctionStatus status, Pageable pageable);

    void cancelAuction(long id);

    AuctionEntity updateTime(Long auctionId, UpdateTimeAuctionRequest request);


    List<AuctionResponse> getAuctionsByUserId();
    List<ListBidForAuction> getBidsByAuctionId(Long auctionId);

    List<AuctionEntity> getAuctionsWin();

    int countUniqueBidders(Long auctionId);


    AuctionEntity reAuction(long id, UpdateTimeAuctionRequest request);

    List<AuctionEntity> getAuctionsBySellerId(Long sellerId);
}
