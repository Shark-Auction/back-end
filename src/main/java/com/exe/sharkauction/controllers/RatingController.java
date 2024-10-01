package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.mappers.IRatingMapper;
import com.exe.sharkauction.models.RatingEntity;
import com.exe.sharkauction.requests.RatingRequest;
import com.exe.sharkauction.services.IRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/rating")
@RequiredArgsConstructor
public class RatingController {
    private final IRatingService ratingService;

    @PostMapping
    public CoreApiResponse<RatingEntity> addRating(
            @Valid @RequestBody RatingRequest request
    ) {
        RatingEntity rating = IRatingMapper.INSTANCE.toModel(request);
        RatingEntity savedRating = ratingService.addRating(rating, null);
        return CoreApiResponse.success(savedRating);
    }
//
//    @PutMapping("/{id}")
//    public CoreApiResponse<RatingEntity> updateRating(
//            @PathVariable Long id,
//            @RequestBody RatingRequest request
//    ) {
//        RatingEntity rating = IRatingMapper.INSTANCE.toModel(request);
//        RatingEntity updatedRating = ratingService.updateRating(id, rating);
//
//        return CoreApiResponse.success(updatedRating);
//    }

    @DeleteMapping("/{id}")
    public void deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<RatingEntity> getRatingById(@PathVariable Long id) {
        RatingEntity rating = ratingService.getRatingById(id);
        return CoreApiResponse.success(rating);
    }

    @GetMapping
    public CoreApiResponse<List<RatingEntity>> getAllRatings() {
        List<RatingEntity> ratings = ratingService.getAllRatings();
        return CoreApiResponse.success(ratings);
    }

    @GetMapping("/product/{productId}")
    public CoreApiResponse<List<RatingEntity>> getRatingsByProduct(@PathVariable Long productId) {
        return CoreApiResponse.success(ratingService.getRatingsByProduct(productId));
    }

    @GetMapping("/customer/{customerId}")
    public CoreApiResponse<List<RatingEntity>> getRatingsByCustomer(@PathVariable Long customerId) {
        return CoreApiResponse.success(ratingService.getRatingsByCustomer(customerId));
    }


}
