package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.CashOutEntity;
import com.exe.sharkauction.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICashOutRepository extends JpaRepository<CashOutEntity, Long> {
    List<CashOutEntity> findByUser(UserEntity user);

}
