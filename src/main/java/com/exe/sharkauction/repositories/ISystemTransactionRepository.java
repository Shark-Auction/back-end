package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.SystemTransactionEntity;
import com.exe.sharkauction.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISystemTransactionRepository extends JpaRepository<SystemTransactionEntity, Long> {

    @Query("SELECT st FROM SystemTransactionEntity st WHERE st.user = :user")
    List<SystemTransactionEntity> findTransactionsByUser(@Param("user") UserEntity user);


}
