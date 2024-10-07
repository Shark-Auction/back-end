package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.OrderType;
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

//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private OrderEntity orderEntity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String checkoutUrl;

    private boolean senderTransaction;
//
//    private boolean buyNow;

    private String toFullName;
    private String toPhoneNumber;
    private String note;
    private String toAddress;

    @Enumerated(EnumType.STRING)
    private OrderType type;
    private String voucherCode;

}
