package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRatingRepository extends JpaRepository<RatingEntity, Long> {
    List<RatingEntity> findByProduct_Id(Long productId);
    List<RatingEntity> findByCustomer_Id(Long customerId);
}
