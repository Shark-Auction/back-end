package com.exe.sharkauction.services;

import com.exe.sharkauction.models.RatingEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IRatingService {
    RatingEntity addRating(RatingEntity rating, List<MultipartFile> images);
    RatingEntity updateRating(Long id, RatingEntity rating);
    void deleteRating(Long id);
    RatingEntity getRatingById(Long id);
    List<RatingEntity> getAllRatings();
    List<RatingEntity> getRatingsByProduct(Long productId);
    List<RatingEntity> getRatingsByCustomer(Long customerId);
}
