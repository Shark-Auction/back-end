package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.models.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IVoucherRepository extends JpaRepository<VoucherEntity, Long> {
    @Query("SELECT v FROM VoucherEntity v WHERE v.voucherCode = :voucherCode")
    VoucherEntity findByVoucherCode(@Param("voucherCode") String voucherCode);

    @Query("SELECT v FROM VoucherEntity v WHERE v.status != 'Used' AND (v.startTime <= :currentDate OR v.endTime >= :currentDate)")
    List<VoucherEntity> findVouchersToUpdate(Date currentDate);

}
