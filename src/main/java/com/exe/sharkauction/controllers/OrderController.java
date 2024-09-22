package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.OrderEntity;
import com.exe.sharkauction.requests.OrderRequest;
import com.exe.sharkauction.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.exe.sharkauction.mappers.IOrderMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/order")
@RequiredArgsConstructor
public class OrderController  {
    private final IOrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public CoreApiResponse<OrderEntity> createOrder(
            @Valid @RequestBody OrderRequest orderRequest){
        OrderEntity order = orderService.createOrder(INSTANCE.toModel(orderRequest));
        return CoreApiResponse.success(order,"Đơn hàng đã được mua thành công");
    }

    @GetMapping("")
    public CoreApiResponse<List<OrderEntity>> getAllOrder(){
        List<OrderEntity> listOrder = orderService.getListOrder();
        return CoreApiResponse.success(listOrder);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<OrderEntity> getOrderById(@Valid @PathVariable Long id){
        OrderEntity order = orderService.getOrderByIdUser(id);
        return CoreApiResponse.success(order);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("admin/{id}")
    public CoreApiResponse<OrderEntity> getOrderByIdAdmin(@Valid @PathVariable Long id){
        OrderEntity order = orderService.getOrderById(id);
        return CoreApiResponse.success(order);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mybuy")
    public CoreApiResponse<List<OrderEntity>> getMyOrderBuy(){
        List<OrderEntity> listOrder = orderService.getOrdersByBuyer();
        return CoreApiResponse.success(listOrder);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mysell")
    public CoreApiResponse<List<OrderEntity>> getMyOrderSell(){
        List<OrderEntity> listOrder = orderService.getOrdersBySeller();
        return CoreApiResponse.success(listOrder);
    }

    @PutMapping("delivered/{id}")
    public CoreApiResponse<?> deliveredProduct(@PathVariable Long id){
        orderService.updateDeliveredProduct(id);
        return CoreApiResponse.success("Cập nhật đơn hàng thành công ");
    }

    @PutMapping("sent/{id}")
    public CoreApiResponse<?> sentProduct(@PathVariable Long id){
        orderService.updateSentProduct(id);
        return CoreApiResponse.success("Cập nhật đơn hàng thành công ");
    }

    @PutMapping("received/{id}")
    public CoreApiResponse<?> receivedProduct(@PathVariable Long id){
        orderService.updateReceivedProduct(id);
        return CoreApiResponse.success("Cập nhật đơn hàng thành công ");
    }

}
