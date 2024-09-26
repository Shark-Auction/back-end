package com.exe.sharkauction.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliverySenderRequest {
    @NotBlank(message = "From name is required")
    private String from_name;

    @NotBlank(message = "From phone is required")
    private String from_phone;

    @NotBlank(message = "From address is required")
    private String from_address;

    @NotBlank(message = "From ward is required")
    private String from_ward_name;

    @NotBlank(message = "From district name is required")
    private String from_district_name;

    @NotBlank(message = "From province is required")
    private String from_province_name;


    //PRODUCT INFORMATION
    private Long productID;

    @NotNull(message = "Payment weight is required")
    private int weight;

    @NotNull(message = "Payment length is required")
    private int length;

    @NotNull(message = "Payment with is required")
    private int width;

    @NotNull(message = "Payment heigh is required")
    private int height;

    private String note;
}
