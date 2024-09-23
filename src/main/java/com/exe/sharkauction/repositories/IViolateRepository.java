package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.ViolateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IViolateRepository extends JpaRepository<ViolateEntity, Long> {

    @Query("SELECT v FROM ViolateEntity v WHERE v.user.id = :userId")
    List<ViolateEntity> findAllByUserId(@Param("userId") Long userId);
}
