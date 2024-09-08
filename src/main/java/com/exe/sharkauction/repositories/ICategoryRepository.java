package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository  extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);

    List<CategoryEntity> findByParent(CategoryEntity parent);



}
