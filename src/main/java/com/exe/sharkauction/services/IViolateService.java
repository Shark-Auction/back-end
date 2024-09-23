package com.exe.sharkauction.services;

import com.exe.sharkauction.models.ViolateEntity;

import java.util.List;

public interface IViolateService {

    ViolateEntity createViolate(ViolateEntity violate);

    ViolateEntity getViolateById(long id );

    List<ViolateEntity> getAllViolate();

    List<ViolateEntity> getViolateByUserId(long userId);

    void deleteViolate(long id);
}
