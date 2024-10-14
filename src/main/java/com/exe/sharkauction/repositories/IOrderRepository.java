package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.OrderEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o FROM OrderEntity o WHERE o.buyer = :buyer")
    List<OrderEntity> findByBuyer(@Param("buyer") UserEntity buyer);

    @Query("SELECT o FROM OrderEntity o WHERE o.product.seller = :seller")
    List<OrderEntity> findBySeller(@Param("seller") UserEntity seller);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM OrderEntity o WHERE o.product = :product")
    boolean existsByProduct(@Param("product") ProductEntity product);

    @Query("SELECT o FROM OrderEntity o WHERE o.product.id = :productId AND o.status = 'PENDING'")
    OrderEntity findByProductIdAndStatusPending(@Param("productId") Long productId);

    @Query("SELECT o FROM OrderEntity o WHERE o.product.id = :productId")
    OrderEntity findByProductId(@Param("productId") Long productId);


    @Query("SELECT SUM(o.price * 0.1) FROM OrderEntity o WHERE o.status = 'received'")
    Float calculateTotalRevenueReceived();

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.status = 'received'")
    Long countReceivedOrders();

    @Query("SELECT COALESCE(SUM(o.price * 0.1), 0) FROM OrderEntity o WHERE o.status = 'received' AND o.orderDate BETWEEN :startDate AND :endDate")
    Float calculateRevenueByDateRange(LocalDate startDate, LocalDate endDate);


    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.status = 'received' AND o.orderDate = :today")
    Long countOrdersByToday(LocalDate today);
}
