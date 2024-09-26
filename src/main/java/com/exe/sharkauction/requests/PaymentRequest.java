package com.exe.sharkauction.requests;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

//    private int orderCode;
    private Long userId;
    private Long orderId;
    private boolean senderTransaction;

    // Getters and Setters for all fields
}
