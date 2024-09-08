package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByName(String name);
    ProductEntity findByName (String name);

    @Query("SELECT p FROM ProductEntity p WHERE p.seller = :seller")
    List<ProductEntity> findProductsBySeller(@Param("seller") UserEntity seller);

}
