package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.BiddingEntity;
import com.exe.sharkauction.requests.BidRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IBiddingMapper {
    IBiddingMapper INSTANCE = Mappers.getMapper(IBiddingMapper.class);

    @Mapping(source = "auctionId", target = "auction.id")
    BiddingEntity toModel(BidRequest request);
}
