package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.exceptions.DataNotFoundException;
import com.exe.sharkauction.components.securities.UserPrincipal;
import com.exe.sharkauction.controllers.WebSocketController;
import com.exe.sharkauction.models.*;
import com.exe.sharkauction.models.enums.AuctionStatus;
import com.exe.sharkauction.models.enums.ProductCondition;
import com.exe.sharkauction.models.enums.ProductStatus;
import com.exe.sharkauction.repositories.IAuctionRepository;
import com.exe.sharkauction.repositories.IAutoBiddingRepository;
import com.exe.sharkauction.repositories.IBiddingRepository;
import com.exe.sharkauction.repositories.IProductRepository;
import com.exe.sharkauction.requests.UpdateTimeAuctionRequest;
import com.exe.sharkauction.responses.AuctionResponse;
import com.exe.sharkauction.responses.ListBidForAuction;
import com.exe.sharkauction.services.IAuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AuctionService implements IAuctionService {
    private final IAuctionRepository auctionRepository;
    private final IProductRepository productRepository;
    private final IBiddingRepository biddingRepository;
    private final IAutoBiddingRepository autoBiddingRepository;
    private final WebSocketController webSocketController;


    @Override
    public AuctionEntity createAuction(AuctionEntity auction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        validateAuctionDuration(auction.getStartTime(), auction.getEndTime());

        if (!isStartTimeValid(auction.getStartTime())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Thời gian bắt đầu phải sau thời gian tạo ít nhất 2 phút.");
        }

        ProductEntity existingProduct = productRepository
                .findById(auction.getProduct().getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Jewelry", "id", auction.getProduct().getId()));


        if (!existingProduct.getSeller().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        List<AuctionEntity> activeAuctions = auctionRepository
                .findActiveAuctionsByProductId(existingProduct.getId());
        if (!activeAuctions.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Sản phẩm đã có phiên đấu giá.");
        }
        existingProduct.setStatus(ProductStatus.AUCTIONING);
        auction.setCurrentPrice(existingProduct.getStartingPrice());
        auction.setProduct(existingProduct);
        auction.setStatus(AuctionStatus.Waiting);

        return auctionRepository.save(auction);
    }


    @Override
    public AuctionEntity getAuctionById(long id) {

        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Phiên đấu giá không tồn tại"));
        auctionRepository.save(auction);
        webSocketController.sendAuctionUpdate(auction);

        return auction;
    }

    @Override
    public List<AuctionEntity> getAllAuctions() {
        return auctionRepository.findAll();
    }


    @Override
    public AuctionEntity updateTime(Long auctionId, UpdateTimeAuctionRequest request) {
        AuctionEntity auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Phiên đấu giá không tồn tại"));

        if (request.getStartTime() != null) {
            auction.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            auction.setEndTime(request.getEndTime());
        }
        return auctionRepository.save(auction);


    }

    @Override
    public List<AuctionEntity> getAuctionsByCategoryId(Long categoryId) {
        return auctionRepository.findAuctionsByCategory(categoryId);
    }

    @Override
    public List<AuctionEntity> getAuctionsByBrandId(Long brandId) {
        return auctionRepository.findAuctionsByBrand(brandId);
    }

    @Override
    public List<AuctionEntity> getMyAuctionsByStatus(AuctionStatus status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return auctionRepository.findAuctionsBySellerAndStatus(user.getId(), status);
    }


    @Override
    public Page<AuctionEntity> searchAuctions(Long collectionId, Long categoryId,
                                              Float minPrice, Float maxPrice, Long brandId,
                                              ProductCondition condition, AuctionStatus status, Pageable pageable) {
        return auctionRepository.searchAuctions(collectionId, categoryId, minPrice, maxPrice, brandId, condition, status, pageable);
    }

    @Override
    public void cancelAuction(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Phiên đấu giá không tồn tại"));

        if (auction.getStatus() != AuctionStatus.Waiting && auction.getStatus() != AuctionStatus.WaitingPay) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Sản phẩm đang được đấu giá không thể huỷ");
        }
        if (!auction.getProduct().getSeller().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        auction.setStatus(AuctionStatus.Cancel);

        auctionRepository.save(auction);
    }

    @Override
    public List<ListBidForAuction> getBidsByAuctionId(Long auctionId) {
        List<BiddingEntity> biddingEntities = biddingRepository.findByAuctionId(auctionId);
        List<AutoBiddingEntity> autoBiddingEntities = autoBiddingRepository.findByAuctionId(auctionId);

        AuctionEntity auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Phiên đấu giá không tồn tại"));

        List<ListBidForAuction> bids = new ArrayList<>();

        for (BiddingEntity bidding : biddingEntities) {
            ListBidForAuction dto = new ListBidForAuction();
            dto.setId(bidding.getId());
            dto.setAuctionId(bidding.getAuction().getId());
            dto.setUserName(bidding.getCustomer().getFull_name());
            dto.setEmail(bidding.getCustomer().getEmail());
            dto.setBidAmount(bidding.getBidAmount());
            dto.setBidTime(bidding.getBidTime());
            dto.setStatus("BIDDING");
            bids.add(dto);
        }

        for (AutoBiddingEntity autoBidding : autoBiddingEntities) {
            if (autoBidding.getMaxBid() < auction.getCurrentPrice()) {
                ListBidForAuction dto = new ListBidForAuction();
                dto.setId(autoBidding.getId());
                dto.setAuctionId(autoBidding.getAuction().getId());
                dto.setUserName(autoBidding.getCustomer().getFull_name());
                dto.setEmail(autoBidding.getCustomer().getEmail());
                dto.setBidAmount(autoBidding.getMaxBid());
                dto.setBidTime(autoBidding.getBidTime());
                dto.setStatus("AUTO_BIDDING");
                bids.add(dto);
            }
        }

        return bids.stream()
                .sorted((b1, b2) -> b1.getBidTime().compareTo(b2.getBidTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AuctionResponse> getAuctionsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        List<BiddingEntity> biddingEntities = biddingRepository.findByCustomerId(user.getId());

        return biddingEntities.stream()
                .map(BiddingEntity::getAuction)
                .filter(auction -> auction.getStatus() == AuctionStatus.InProgress)
                .distinct()
                .map(auction -> {
                    AuctionResponse dto = new AuctionResponse();
                    dto.setId(auction.getId());
                    dto.setProductName(auction.getProduct().getName());
                    dto.setStartTime(auction.getStartTime());
                    dto.setEndTime(auction.getEndTime());
                    dto.setCurrentPrice(auction.getCurrentPrice());
                    dto.setStatus(auction.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AuctionEntity> getAuctionsWin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return auctionRepository.findByWinnerAndStatus(user, AuctionStatus.Completed);
    }

    private void validateAuctionDuration(Date startTime, Date endTime) {
        if (startTime.after(endTime)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc");
        }

        Instant startInstant = startTime.toInstant();
        Instant endInstant = endTime.toInstant();

        Duration duration = Duration.between(startInstant, endInstant);

        if (duration.toDays() < 1 || duration.toDays() > 7) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Phiên đấu giá phải diễn ra từ 1 - 7 ngày");
        }
    }

    private boolean isStartTimeValid(Date startTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = startTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return start.isAfter(now.plusMinutes(2));
    }

    @Override
    public int countUniqueBidders(Long auctionId) {
        List<Long> bidderIds = biddingRepository.findDistinctBiddersByAuctionId(auctionId);
        return bidderIds.size();
    }



    @Override
    public AuctionEntity reAuction(long id, UpdateTimeAuctionRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Phiên đấu giá không tồn tại"));
        if (!auction.getProduct().getSeller().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        ProductEntity jewelry = auction.getProduct();

        validateAuctionDuration(request.getStartTime(), request.getEndTime());

        if (!isStartTimeValid(request.getStartTime())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Thời gian bắt đầu phải sau thời gian tạo ít nhất 2 phút.");
        }

        auction.setCurrentPrice(jewelry.getStartingPrice());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());
        auction.setStatus(AuctionStatus.Waiting);

        return auctionRepository.save(auction);
    }

    public List<AuctionEntity> getAuctionsBySellerId(Long sellerId) {
        return auctionRepository.findAuctionsBySellerId(sellerId);
    }
}
