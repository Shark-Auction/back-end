package com.exe.sharkauction.requests;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseRequest {
    private String paymentLinkId; // Payment Link Id
    private boolean cancel; // Trạng thái hủy
    private String status; // Trạng thái thanh toán
}
