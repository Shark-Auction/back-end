package com.exe.sharkauction.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherRequest {

    private String voucherCode;

    private float discount;

    private long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;
}
