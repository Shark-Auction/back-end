package com.exe.sharkauction.controllers;

import com.exe.sharkauction.responses.RevenueDashboardResponse;
import com.exe.sharkauction.services.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

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
        // Nếu startDate và endDate không được cung cấp, lấy dữ liệu cho tuần hiện tại
        if (startDate == null && endDate == null) {
            // Lấy ngày đầu tiên của tuần hiện tại
            LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            // Lấy ngày cuối cùng của tuần hiện tại (Chủ Nhật)
            LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            // Gọi các phương thức của orderService với startDate và endDate là startOfWeek và endOfWeek
            Long ordersToday = orderService.countOrdersByToday();
            Float revenueByDateRange = orderService.calculateRevenueByDateRange(startOfWeek, endOfWeek);
            Long receivedOrders = orderService.countReceivedOrders();
            Float totalRevenue = orderService.calculateTotalRevenue();

            return new RevenueDashboardResponse(ordersToday, revenueByDateRange, receivedOrders, totalRevenue);
        } else {
            // Ngược lại, nếu startDate hoặc endDate được cung cấp, sử dụng chúng để tính toán
            Long ordersToday = orderService.countOrdersByToday();
            Float revenueByDateRange = orderService.calculateRevenueByDateRange(startDate, endDate);
            Long receivedOrders = orderService.countReceivedOrders();
            Float totalRevenue = orderService.calculateTotalRevenue();

            return new RevenueDashboardResponse(ordersToday, revenueByDateRange, receivedOrders, totalRevenue);
        }
    }
}
