package com.exe.sharkauction.scheduled;

import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.enums.AuctionStatus;
import com.exe.sharkauction.models.enums.ProductStatus;
import com.exe.sharkauction.repositories.IAuctionRepository;
import com.exe.sharkauction.repositories.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuctionSchedule {
    private final IAuctionRepository auctionRepository;
    private final IProductRepository productRepository;

    @Scheduled(fixedRate = 3000)
    public void updateStatusAuction() {
        List<AuctionEntity> auctions = auctionRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (AuctionEntity auction : auctions) {
            ProductEntity product = auction.getProduct();

            LocalDateTime startTime = LocalDateTime.ofInstant(auction.getStartTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime endTime = LocalDateTime.ofInstant(auction.getEndTime().toInstant(), ZoneId.systemDefault());

            if (auction.getStatus() == AuctionStatus.Waiting && startTime.isBefore(now)) {
                product.setStatus(ProductStatus.AUCTIONING);
                auction.setStatus(AuctionStatus.InProgress);
                auctionRepository.save(auction);

            }

            if (auction.getStatus() == AuctionStatus.InProgress && endTime.isBefore(now)) {
                if (auction.getWinner() != null) {
                    auction.setStatus(AuctionStatus.Completed);
                    product.setStatus(ProductStatus.AUCTIONSUCCESS);
                    product.setFinalPrice(auction.getCurrentPrice());
                }
                else {
                    auction.setStatus(AuctionStatus.Fail);
                    product.setStatus(ProductStatus.AUCTIONFAIL);
                }

            }
            productRepository.save(product);
            auctionRepository.save(auction);
        }
    }
}
