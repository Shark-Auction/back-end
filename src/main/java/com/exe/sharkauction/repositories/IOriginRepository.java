package com.exe.sharkauction.repositories;


import com.exe.sharkauction.models.OriginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IOriginRepository extends JpaRepository<OriginEntity, Long> {
    boolean existsByName(String name);
    @Query("SELECT b FROM OriginEntity b WHERE LOWER(b.name) = LOWER(:name)")
    OriginEntity findByName(@Param("name") String name);
}
