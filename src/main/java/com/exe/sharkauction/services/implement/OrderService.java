package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.securities.UserPrincipal;
import com.exe.sharkauction.models.OrderEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.models.enums.OrderStatus;
import com.exe.sharkauction.models.enums.OrderType;
import com.exe.sharkauction.models.enums.ProductStatus;
import com.exe.sharkauction.repositories.IOrderRepository;
import com.exe.sharkauction.repositories.IProductRepository;
import com.exe.sharkauction.repositories.IUserRepository;
import com.exe.sharkauction.services.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;

    private final IUserRepository userRepository;

    private final IProductRepository productRepository;
    @Override
    public OrderEntity createOrder(OrderEntity order) {
        // Lấy thông tin người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        ProductEntity product = productRepository.findById(order.getProduct().getId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại"));

        boolean productExistsInOrder = orderRepository.existsByProduct(product);
        if (productExistsInOrder) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Sản phẩm này đã tồn tại trong đơn hàng khác.");
        }

        if (order.getType() == OrderType.BuyNow) {
            if (product.isBuyNow()) {
                order.setPrice(product.getBuyNowPrice());
                product.setStatus(ProductStatus.SOLD);
            } else {
                throw new AppException(HttpStatus.BAD_REQUEST, "Sản phẩm này không hỗ trợ mua ngay.");
            }
        } else if (order.getType() == OrderType.Auction) {
            if (product.getStatus() == ProductStatus.AUCTIONSUCCESS) {
                order.setPrice(product.getFinalPrice());
            } else {
                throw new AppException(HttpStatus.BAD_REQUEST, "Không thể đặt hàng vì cuộc đấu giá chưa hoàn thành");
            }
        } else {
            throw new AppException(HttpStatus.BAD_REQUEST, "Loại đơn hàng không hợp lệ.");
        }

        order.setBuyer(user);
        order.setStatus(OrderStatus.processing);
        order.setOrderDate(LocalDate.now());

        return orderRepository.save(order);
    }

    @Override
    public List<OrderEntity> getListOrder(){
        return orderRepository.findAll();
    }
    @Override
    public OrderEntity getOrderByIdUser(long id ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Đơn hàng không tồn tại"));
        if (order.getProduct().getSeller().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        return order;
    }
    @Override
    public OrderEntity getOrderById(long id ){
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Đơn hàng không tồn tại"));
        return order;
    }

    @Override
    public List<OrderEntity> getOrdersByBuyer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return orderRepository.findByBuyer(user);
    }
    @Override
    public List<OrderEntity> getOrdersBySeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return orderRepository.findBySeller(user);
    }
    @Override
    public void updateSentProduct(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Đơn hàng không tồn tại"));
        if (order.getProduct().getSeller().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        order.setStatus(OrderStatus.shipping);
    }

    @Override
    public void updateDeliveredProduct(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Đơn hàng không tồn tại"));
        if (order.getProduct().getSeller().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        order.setStatus(OrderStatus.delivered);
    }
    @Override
    public void updateReceivedProduct(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Đơn hàng không tồn tại"));
        if (order.getBuyer().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        order.setStatus(OrderStatus.received);
    }
}
