package com.exe.sharkauction.controllers;

import com.exe.sharkauction.responses.RevenueDashboardResponse;
import com.exe.sharkauction.services.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("${app.api.version.v1}/dashboards")
@RequiredArgsConstructor
public class DashboardController {
    private final IOrderService orderService;

    @GetMapping("/revenue")
    public RevenueDashboardResponse getOrderStats(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {

        Long ordersToday = orderService.countOrdersByToday();
        Float revenueByDateRange = orderService.calculateRevenueByDateRange(startDate, endDate);
        Long receivedOrders = orderService.countReceivedOrders();
        Float totalRevenue = orderService.calculateTotalRevenue();

        return new RevenueDashboardResponse(ordersToday, revenueByDateRange, receivedOrders, totalRevenue);
    }
}
