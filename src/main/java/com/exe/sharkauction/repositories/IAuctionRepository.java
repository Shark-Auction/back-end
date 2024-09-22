package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.models.enums.AuctionStatus;
import com.exe.sharkauction.models.enums.ProductCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAuctionRepository extends JpaRepository<AuctionEntity, Long> {


    @Query("SELECT a FROM AuctionEntity a WHERE " +
            "(:categoryId IS NULL OR a.product.category.id = :categoryId)")
    List<AuctionEntity> findAuctionsByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT a FROM AuctionEntity a WHERE " +
            "(:brandId IS NULL OR a.product.brand.id = :brandId)")
    List<AuctionEntity> findAuctionsByBrand(@Param("brandId") Long brandId);

    @Query("SELECT a FROM AuctionEntity a WHERE " +
            "(:originId IS NULL OR a.product.origin.id = :originId)")
    List<AuctionEntity> findAuctionsByOrigin(@Param("originId") Long originId);


    @Query("SELECT a FROM AuctionEntity a WHERE " +
            "(:sellerId IS NULL OR a.product.seller.id = :sellerId) AND " +
            "(:status IS NULL OR a.status = :status)")
    List<AuctionEntity> findAuctionsBySellerAndStatus(
            @Param("sellerId") Long sellerId,
            @Param("status") AuctionStatus status);

    @Query("SELECT a FROM AuctionEntity a WHERE " +
            "(:collectionId IS NULL OR a.product.category.id = :collectionId) AND " +
            "(:categoryId IS NULL OR a.product.category.id = :categoryId) AND " +
            "(:minPrice IS NULL OR a.currentPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR a.currentPrice <= :maxPrice) AND " +
            "(:brandId IS NULL OR a.product.brand.id = :brandId) AND " +
            "(:condition IS NULL OR a.product.condition = :condition) AND " +
            "(:status IS NULL OR a.status = :status)")
    Page<AuctionEntity> searchAuctions(
            @Param("collectionId") Long collectionId,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice,
            @Param("brandId") Long brandId,
            @Param("condition") ProductCondition condition,
            @Param("status") AuctionStatus status,
            Pageable pageable);

    @Query("SELECT a FROM AuctionEntity a WHERE a.product.id = :productId AND a.status NOT IN ('FAIL', 'CANCEL')")
    List<AuctionEntity> findActiveAuctionsByProductId(@Param("productId") Long productId);

    @Query("SELECT a FROM AuctionEntity a WHERE a.winner = :winner AND a.endTime < CURRENT_TIMESTAMP")
    List<AuctionEntity> findByWinnerAndEnded(@Param("winner") UserEntity winner);
    @Query("SELECT a FROM AuctionEntity a WHERE a.product.seller.id = :sellerId")
    List<AuctionEntity> findAuctionsBySellerId(@Param("sellerId") Long sellerId);

}