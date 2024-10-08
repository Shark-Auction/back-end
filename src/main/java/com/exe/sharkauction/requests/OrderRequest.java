package com.exe.sharkauction.requests;

import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.enums.OrderType;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String fullName;

    private String phoneNumber;

    private String note;

    private Long product_id;


    private String toAddress;

    private OrderType type;

    private float discount;
}
