package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.AuctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVoucherRepository extends JpaRepository<AuctionEntity, String> {

}
