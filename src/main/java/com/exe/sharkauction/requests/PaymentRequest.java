package com.exe.sharkauction.requests;

import com.exe.sharkauction.models.enums.OrderType;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

//    private int orderCode;
//    private Long userId;
    private Long productId;
    private boolean senderTransaction;
//    private boolean buyNow;

    //Order
    private String toFullName;
    private String toPhoneNumber;
    private String note;

    private String toAddress;
    private OrderType type;
    private String voucherCode;
}
