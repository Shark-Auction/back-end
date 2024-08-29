package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository  extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);

}
