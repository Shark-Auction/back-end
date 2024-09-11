package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.requests.AuctionRequest;
import com.exe.sharkauction.requests.UpdateTimeAuctionRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IAuctionMapper {
    IAuctionMapper INSTANCE = Mappers.getMapper(IAuctionMapper.class);
    @Mapping(source = "productId", target = "product.id")
    AuctionEntity toModel(AuctionRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAuctionFromRequest(UpdateTimeAuctionRequest request, @MappingTarget AuctionEntity auction);
}
