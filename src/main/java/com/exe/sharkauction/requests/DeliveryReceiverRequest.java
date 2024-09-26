package com.exe.sharkauction.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReceiverRequest {
    /*
    * token: 43a8eec9-7b4c-11ef-b441-069be3e54cb9
    * required_note: CHOXEMHANGKHONGTHU
    * shop_id: 	5347335
    * service_type_id: https://api.ghn.vn/home/docs/detail?id=77
    * payment_type_id: 2 - Buyer pay
    * */


//    @NotBlank(message = "Required note cannot be blank")
//    private String required_note;
//    private String return_phone;
//    private String return_address;
//    private Integer return_district_id;
//    private String return_ward_code;
//    private String client_order_code;
    //TO PERSON INFORMATION
    @NotNull(message = "Payment type is required")
    private int payment_type_id;

    @NotNull(message = "Service type is required")
    private int service_type_id;

    @NotBlank(message = "To name is required")
    private String to_name;

    @NotBlank(message = "To phone is required")
    private String to_phone;

    @NotBlank(message = "To address is required")
    private String to_address;

    @NotBlank(message = "To ward code is required")
    private String to_ward_code;

    @NotNull(message = "To district ID is required")
    private int to_district_id;

    @NotNull(message = "Payment productID is required")
    private Long productID;

//    @NotNull(message = "COD amount is required")
//    private int cod_amount;

}
