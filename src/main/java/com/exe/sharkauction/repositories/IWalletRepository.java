package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.models.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWalletRepository extends JpaRepository<WalletEntity, Long> {
    WalletEntity findByUser(UserEntity user);
}
