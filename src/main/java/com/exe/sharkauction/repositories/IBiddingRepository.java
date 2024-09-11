package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.BiddingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBiddingRepository extends JpaRepository<BiddingEntity, Long> {
    @Query("SELECT b FROM BiddingEntity b WHERE b.auction.id = :auctionId")
    List<BiddingEntity> findByAuctionId(@Param("auctionId") long auctionId);


    List<BiddingEntity> findByCustomerId(Long customerId);

    @Query("SELECT DISTINCT b.customer.id FROM BiddingEntity b WHERE b.auction.id = :auctionId")
    List<Long> findDistinctBiddersByAuctionId(Long auctionId);

}