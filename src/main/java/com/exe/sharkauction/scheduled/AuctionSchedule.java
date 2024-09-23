package com.exe.sharkauction.scheduled;

import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.ViolateEntity;
import com.exe.sharkauction.models.enums.*;
import com.exe.sharkauction.repositories.IAuctionRepository;
import com.exe.sharkauction.repositories.IProductRepository;
import com.exe.sharkauction.repositories.IViolateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuctionSchedule {
    private final IAuctionRepository auctionRepository;
    private final IProductRepository productRepository;
    private final IViolateRepository violateRepository;

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
                    if (product.getFinalPrice() < product.getDesiredPrice()) {
                        auction.setStatus(AuctionStatus.WaitingConfirm);
                        product.setStatus(ProductStatus.AUCTIONSUCCESS);
                    } else {
                        auction.setStatus(AuctionStatus.WaitingPay);
                        product.setStatus(ProductStatus.AUCTIONSUCCESS);
                    }
                }
                else {
                    auction.setStatus(AuctionStatus.Fail);
                    product.setStatus(ProductStatus.AUCTIONFAIL);
                }

            }

            if (auction.getStatus() == AuctionStatus.WaitingConfirm) {
                Duration durationSinceEnd = Duration.between(endTime, now);
                if (durationSinceEnd.toDays() >= 3) {
                    auction.setStatus(AuctionStatus.Cancel);
                    product.setStatus(ProductStatus.AUCTIONFAIL);

                }
            }

            if (auction.getStatus() == AuctionStatus.WaitingPay) {
                Duration durationSinceEnd = Duration.between(endTime, now);
                if (durationSinceEnd.toDays() >= 7) {
                    auction.setStatus(AuctionStatus.Fail);
                    product.setStatus(ProductStatus.AUCTIONFAIL);
                    ViolateEntity violate = new ViolateEntity();
                    violate.setUser(auction.getWinner());
                    violate.setType(ViolateType.non_payment);
                    violate.setDescription("Bạn chưa thanh toán đơn hàng đúng hạn");
                    violate.setStatus(ViolateStatus.PENALIZED);
                    violateRepository.save(violate);
                }
            }

            productRepository.save(product);
            auctionRepository.save(auction);
        }
    }
}
