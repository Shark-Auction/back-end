package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITokenRepository extends JpaRepository<TokenEntity, Long> {
}

