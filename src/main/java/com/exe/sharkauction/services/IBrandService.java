package com.exe.sharkauction.services;

import com.exe.sharkauction.models.BrandEntity;

import java.util.List;

public interface IBrandService {
    BrandEntity createBrand(BrandEntity brand) ;
    BrandEntity   getBrandById(long id);
    List<BrandEntity> getAllBrands();
    BrandEntity updateBrand(long brandId, BrandEntity category);
    void deleteBrand(long id);
}
