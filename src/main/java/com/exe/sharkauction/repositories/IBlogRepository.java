package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlogRepository extends JpaRepository<BlogEntity, Long> {
}
