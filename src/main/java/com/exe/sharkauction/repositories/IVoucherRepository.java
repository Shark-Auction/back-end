package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.models.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IVoucherRepository extends JpaRepository<VoucherEntity, Long> {
    @Query("SELECT v FROM VoucherEntity v WHERE v.voucherCode = :voucherCode")
    VoucherEntity findByVoucherCode(@Param("voucherCode") String voucherCode);

}
