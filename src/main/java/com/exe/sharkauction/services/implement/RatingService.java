package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.constants.ImageContants;
import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.exceptions.DataNotFoundException;
import com.exe.sharkauction.components.utils.UploadImagesUtils;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.RatingEntity;
import com.exe.sharkauction.models.RatingMediaEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.models.enums.ProductStatus;
import com.exe.sharkauction.repositories.IProductRepository;
import com.exe.sharkauction.repositories.IRatingRepository;
import com.exe.sharkauction.repositories.IUserRepository;
import com.exe.sharkauction.services.IRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService implements IRatingService {
    private final IRatingRepository ratingRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;

    @Override
    public RatingEntity addRating(RatingEntity rating, List<MultipartFile> images) {
        if (rating.getCustomer().getId() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Customer information is missing.");
        }

        Long customerId = rating.getCustomer().getId();
        Long productId = rating.getProduct().getId();
        String IMAGE_PATH = ImageContants.RATING_IMAGE_PATH;

        // Fetch user and product in parallel
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + customerId));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new UsernameNotFoundException("Product not found with id : " + productId));

        validateUserRole(customer);
//        validateProductStatus(product);

//        // Store images and associate them with the rating
//        List<RatingMediaEntity> ratingMediaEntities = new ArrayList<>();
//        if (images != null && !images.isEmpty()) {
//            for (MultipartFile image : images) {
//                try {
//                    String storedFileName = UploadImagesUtils.storeFile(image, IMAGE_PATH);
//                    RatingMediaEntity ratingMedia = new RatingMediaEntity();
//                    ratingMedia.setMediaUrl(storedFileName);
//                    ratingMedia.setRatingEntity(rating);
//                    ratingMediaEntities.add(ratingMedia);
//                } catch (IOException e) {
//                    throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store image file");
//                }
//            }
//        }
//        rating.setMedia(ratingMediaEntities);

        // Save and return the rating
        return ratingRepository.save(rating);
    }

    @Override
    public RatingEntity updateRating(Long id, RatingEntity rating) {
        Optional<RatingEntity> existingRating = Optional.ofNullable(ratingRepository
                .findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Rating", "id", id)));
        if (existingRating.isPresent()) {
            RatingEntity updatedRating = existingRating.get();
            updatedRating.setRatingValue(rating.getRatingValue());
            updatedRating.setReview(rating.getReview());
            updatedRating.setMedia(rating.getMedia());
            return ratingRepository.save(updatedRating);
        }
        return null; // or throw an exception for not found
    }

    @Override
    public void deleteRating(Long id) {
        if (!ratingRepository.existsById(id)) {
            throw new DataNotFoundException("Rating", "id", id);
        }
        ratingRepository.deleteById(id);
    }

    @Override
    public RatingEntity getRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Rating", "id", id));
    }

    @Override
    public List<RatingEntity> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public List<RatingEntity> getRatingsByProduct(Long productId) {
        return ratingRepository.findByProduct_Id(productId);
    }

    @Override
    public List<RatingEntity> getRatingsByCustomer(Long customerId) {
        return ratingRepository.findByCustomer_Id(customerId);
    }

    private void validateUserRole(UserEntity customer) {
        // Replace with your actual role ID check
        Long requiredRoleId = 4L;  // Example role ID
        if (!customer.getRole_id().getId().equals(requiredRoleId)) {
            throw new RuntimeException("User does not have permission to rate");
        }
    }

//    private void validateProductStatus(ProductEntity product) {
//        // Replace with your actual product status check
//        ProductStatus requiredStatus = ProductStatus.DELIVERED;  // Example status
//        if (!product.getStatus().equals(requiredStatus)) {
//            throw new RuntimeException("Product is not in a valid state for rating");
//        }
//    }
}
