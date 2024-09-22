package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.DeliveryMethod;
import com.exe.sharkauction.models.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private UserEntity buyer;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "phone_number",nullable = false, length = 100)
    private String phoneNumber;

    @Column(name = "note", length = 100)
    private String note;

    @Column(name="order_date")
    private LocalDate orderDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private float price;

    @ManyToOne
    @JoinColumn(name="product_id")
    private ProductEntity product;

    @Column(name = "to_address")
    private String toAddress;

    @Column(name = "send_date")
    private LocalDate sendDate;

    @Column(name = "received_date")
    private LocalDate receivedDate;

}
