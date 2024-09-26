package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
