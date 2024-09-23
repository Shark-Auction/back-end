package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.BlogImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlogImageRepository extends JpaRepository<BlogImageEntity, Long> {
}
