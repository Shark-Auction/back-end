package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByPaymentID(String paymentID);
}
