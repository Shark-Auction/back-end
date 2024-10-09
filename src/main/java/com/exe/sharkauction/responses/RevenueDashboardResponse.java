package com.exe.sharkauction.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueDashboardResponse {

    private Long ordersToday;

    private Float revenueByDateRange;

    private Long receivedOrders;

    private Float totalRevenue;
}
