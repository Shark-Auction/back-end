package com.exe.sharkauction.mappers;

import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.models.OriginEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.RatingEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.repositories.IProductRepository;
import com.exe.sharkauction.repositories.IUserRepository;
import com.exe.sharkauction.requests.OriginRequest;
import com.exe.sharkauction.requests.RatingRequest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring")
public interface IRatingMapper {
    IRatingMapper INSTANCE = Mappers.getMapper(IRatingMapper.class);

    @Mapping(target = "customer.id", source = "customerId")
    @Mapping(target = "product.id", source = "productId")
    RatingEntity toModel(RatingRequest request);
//
//    @Named("mapCustomer")
//    default UserEntity mapCustomer(Long customerId, @Context IUserRepository userRepository) {
//        return userRepository.findById(customerId)
//                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Invalid customer ID"));
//    }
//
//    @Named("mapProduct")
//    default ProductEntity mapProduct(Long productId, @Context IProductRepository productRepository) {
//        return productRepository.findById(productId)
//                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Invalid product ID"));
//    }
}
