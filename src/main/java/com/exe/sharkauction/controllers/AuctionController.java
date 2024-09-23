package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.models.enums.AuctionStatus;
import com.exe.sharkauction.models.enums.ProductCondition;
import com.exe.sharkauction.requests.AuctionRequest;
import com.exe.sharkauction.requests.UpdateTimeAuctionRequest;
import com.exe.sharkauction.responses.AuctionResponse;
import com.exe.sharkauction.responses.ListBidForAuction;
import com.exe.sharkauction.services.IAuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.exe.sharkauction.mappers.IAuctionMapper.INSTANCE;
@RestController
@RequestMapping("${app.api.version.v1}/auction")
@RequiredArgsConstructor
public class AuctionController {
    private final IAuctionService auctionService;
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public CoreApiResponse<AuctionEntity> createAuction(
            @Valid @RequestBody AuctionRequest auctionRequest
    ){
        AuctionEntity auction = auctionService.createAuction(INSTANCE.toModel(auctionRequest));
        return CoreApiResponse.success(auction,"Tạo cuộc đấu giá thành công");
    }

    @GetMapping("")
    public CoreApiResponse<List<AuctionEntity>> getAllAuctions(){
        List<AuctionEntity> auctions = auctionService.getAllAuctions();
        return CoreApiResponse.success(auctions);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<AuctionEntity> getAuctionById(@Valid @PathVariable Long id){
        AuctionEntity auction = auctionService.getAuctionById(id);
        return CoreApiResponse.success(auction);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/time/{id}")
    public CoreApiResponse<AuctionEntity> updateTime(@PathVariable Long id, @RequestBody UpdateTimeAuctionRequest request) {
        AuctionEntity updatedAuction = auctionService.updateTime(id, request);
        return CoreApiResponse.success(updatedAuction,"Cập nhật đấu giá thành công");
    }

    @GetMapping("/category/{categoryId}")
    public CoreApiResponse<List<AuctionEntity>> getAuctionsByCategoryId(@PathVariable Long categoryId) {
        return CoreApiResponse.success(auctionService.getAuctionsByCategoryId(categoryId));
    }

    @GetMapping("/brand/{brandId}")
    public CoreApiResponse<List<AuctionEntity>> getAuctionsByBrandId(@PathVariable Long brandId) {
        return CoreApiResponse.success(auctionService.getAuctionsByBrandId(brandId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/myauction")
    public CoreApiResponse<List<AuctionEntity>>getAuctionsBySellerIdAndStatus(
            @RequestParam(required = false) AuctionStatus status) {
        return CoreApiResponse.success(auctionService.getMyAuctionsByStatus(status));
    }

    @GetMapping("/viewauction")
    public CoreApiResponse<Page<AuctionEntity>> viewAuction(
            @RequestParam(required = false) Long collectionId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) ProductCondition condition,
            @RequestParam(required = false) AuctionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );
        return CoreApiResponse.success(auctionService.searchAuctions(collectionId, categoryId, minPrice, maxPrice, brandId, condition, AuctionStatus.InProgress, pageRequest));
    }

    @GetMapping("/admin/search")
    public Page<AuctionEntity> searchAuctions(
            @RequestParam(required = false) Long collectionId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) ProductCondition condition,
            @RequestParam(required = false) AuctionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );
        return auctionService.searchAuctions(collectionId, categoryId, minPrice, maxPrice, brandId, condition, status,pageRequest);
    }

    @DeleteMapping("/cancel/{id}")
    public CoreApiResponse<?> cancalAuctions(
            @PathVariable Long id
    ){
        auctionService.cancelAuction(id);
        return CoreApiResponse.success("Huỷ đấu giá thành công");
    }



    @GetMapping("/bids/{auctionId}")
    public CoreApiResponse<List<ListBidForAuction>> getBidsByAuctionId(@PathVariable Long auctionId) {
        return CoreApiResponse.success(auctionService.getBidsByAuctionId(auctionId)) ;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/join/user")
    public CoreApiResponse<List<AuctionResponse>>getAuctionsByUserId() {
        return CoreApiResponse.success(auctionService.getAuctionsByUserId());
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/win")
    public CoreApiResponse<List<AuctionEntity>> getAuctionWin() {
        return CoreApiResponse.success(auctionService.getAuctionsWin());
    }

    @GetMapping("/bidders/count/{auctionId}")
    public CoreApiResponse<?> getUniqueBidderCount(@PathVariable Long auctionId) {
        int count = auctionService.countUniqueBidders(auctionId);
        return CoreApiResponse.success("Total bidders: " + count);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/reAuction/{id}")
    public CoreApiResponse<AuctionEntity> reAuction(@PathVariable Long id, @RequestBody UpdateTimeAuctionRequest request) {
        AuctionEntity updatedAuction = auctionService.reAuction(id, request);
        return CoreApiResponse.success(updatedAuction,"Cập nhật đấu giá thành công");
    }

    @GetMapping("/seller/{sellerId}")
    public List<AuctionEntity> getAuctionsBySellerId(@PathVariable Long sellerId) {
        return auctionService.getAuctionsBySellerId(sellerId);
    }

    @PutMapping("/confirm/{id}")
    public CoreApiResponse<?> confirmAuctions(
            @PathVariable Long id
    ){
        auctionService.comfirmAuctionForSeller(id);
        return CoreApiResponse.success("Xác nhận cuộc đấu giá thành công");
    }
}
