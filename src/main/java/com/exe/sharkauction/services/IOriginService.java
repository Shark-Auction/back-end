package com.exe.sharkauction.services;

import com.exe.sharkauction.models.OriginEntity;

import java.util.List;

public interface IOriginService {
    OriginEntity createOrigin(OriginEntity Origin) ;
    OriginEntity   getOriginById(long id);
    List<OriginEntity> getAllOrigins();
    OriginEntity updateOrigin(long originId, OriginEntity origin);
    void deleteOrigin(long id);

}
