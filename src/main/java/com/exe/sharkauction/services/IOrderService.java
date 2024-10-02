package com.exe.sharkauction.services;

import com.exe.sharkauction.models.OrderEntity;

import java.util.List;

public interface IOrderService {

    OrderEntity createOrder(OrderEntity order, String voucherCode);

    List<OrderEntity> getListOrder();

    OrderEntity getOrderById(long id );

    OrderEntity getOrderByIdUser(long id );
    List<OrderEntity> getOrdersByBuyer();

    List<OrderEntity> getOrdersBySeller();

    void updateSentProduct(long id);

    void updateDeliveredProduct(long id);

    void updateReceivedProduct(long id);

}
