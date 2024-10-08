package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.requests.ProductRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface IProductMapper {
    IProductMapper INSTANCE = Mappers.getMapper(IProductMapper.class);
    @Mapping(source = "brandName", target = "brand.name")
    @Mapping(source = "categoryId",target = "category.id")
    @Mapping(source = "originName",target = "origin.name")
    ProductEntity toModel(ProductRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "brandName", target = "brand.name")
    @Mapping(source = "categoryId",target = "category.id")
    @Mapping(source = "originName",target = "origin.name")
    void updateProductFromRequest(ProductRequest request, @MappingTarget ProductEntity product);
}
