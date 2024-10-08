package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "deliveries")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderCode;

    // Receiver Information
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;
    private int paymentTypeId;
    private int serviceTypeId;
    private String fromName;
    private String fromPhone;
    private String fromAddress;
    private String fromWardName;
    private String fromDistrictName;
    private String fromProvinceName;
    private Long productId; // Will be updated in step 2

    // Sender Information
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;
    private String toName;
    private String toPhone;
    private String toAddress;
    private String toWardCode;
    private int toDistrictId;
    private int weight;
    private int length;
    private int width;
    private int height;
    private String note;

    // Additional fields for later API calls (optional)
    private String requiredNote = "CHOXEMHANGKHONGTHU";
    private String returnPhone;
    private String returnAddress;

    private DeliveryStatus status;

    private List<Object> deliveryStatus;
}
