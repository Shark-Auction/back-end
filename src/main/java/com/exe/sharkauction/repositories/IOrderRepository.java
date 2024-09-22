package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.OrderEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o FROM OrderEntity o WHERE o.buyer = :buyer")
    List<OrderEntity> findByBuyer(@Param("buyer") UserEntity buyer);

    @Query("SELECT o FROM OrderEntity o WHERE o.product.seller = :seller")
    List<OrderEntity> findBySeller(@Param("seller") UserEntity seller);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM OrderEntity o WHERE o.product = :product")
    boolean existsByProduct(@Param("product") ProductEntity product);
}
