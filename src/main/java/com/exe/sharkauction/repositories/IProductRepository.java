package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByName(String name);
    ProductEntity findByName (String name);

}
