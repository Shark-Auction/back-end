package com.exe.sharkauction.responses;

import com.exe.sharkauction.models.enums.AuctionStatus;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionResponse {
    private Long id;

    private String productName;

    private Date startTime;

    private Date endTime;

    private float currentPrice;

    private AuctionStatus status;
}
