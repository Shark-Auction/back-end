package com.exe.sharkauction.services;

import com.exe.sharkauction.models.OrderEntity;

import java.time.LocalDate;
import java.util.List;

public interface IOrderService {

    OrderEntity createOrder(OrderEntity order);

    List<OrderEntity> getListOrder();

    OrderEntity getOrderById(long id );

    OrderEntity getOrderByIdUser(long id );
    List<OrderEntity> getOrdersByBuyer();

    List<OrderEntity> getOrdersBySeller();

    void updateSentProduct(long id);

    void updateDeliveredProduct(long id);

    void updateReceivedProduct(long id);

    Long countOrdersByToday();

    Float calculateRevenueByDateRange(LocalDate startDate, LocalDate endDate);

    Long countReceivedOrders();

    Float calculateTotalRevenue();
}
