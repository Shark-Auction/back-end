package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.PaymentStatus;
import com.exe.sharkauction.requests.PaymentRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "payments")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int orderCode;
    private String paymentID;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "payment_user_id")
    private UserEntity paymentUser;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String checkoutUrl;

    private boolean senderTransaction;
}
