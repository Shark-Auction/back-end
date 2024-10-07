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
    private Long productId;
    private boolean senderTransaction;
    private boolean buyNow;

    // Getters and Setters for all fields
}
