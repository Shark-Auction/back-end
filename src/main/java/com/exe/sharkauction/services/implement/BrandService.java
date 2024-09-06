package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.utils.StringUtils;
import com.exe.sharkauction.models.BrandEntity;
import com.exe.sharkauction.repositories.IBrandRepository;
import com.exe.sharkauction.services.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService implements IBrandService {
    private final IBrandRepository brandRepository;

    @Override
    public BrandEntity createBrand(BrandEntity category) {
        category.setName(StringUtils.NameStandardlizing(category.getName()));
        if(brandRepository.existsByName(category.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "That name is existed!");
        }
        return brandRepository.save(category);
    }

    @Override
    public BrandEntity getBrandById(long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This brand is not existed!"));
    }

    @Override
    public List<BrandEntity> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public BrandEntity updateBrand(long brandId, BrandEntity brand) {
        brand.setName(StringUtils.NameStandardlizing(brand.getName()));
        if(brandRepository.existsByName(brand.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "That name is existed!");
        }
        BrandEntity existingBrand = this.getBrandById(brandId);
        existingBrand.setName(brand.getName());
        return brandRepository.save(existingBrand);
    }

    @Override
    public void deleteBrand(long id) {
        BrandEntity existingBrand = getBrandById(id);
        brandRepository.delete(existingBrand);
    }
}
