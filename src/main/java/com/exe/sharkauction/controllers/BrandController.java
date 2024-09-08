package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.BrandEntity;
import com.exe.sharkauction.requests.BrandRequest;
import com.exe.sharkauction.services.IBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import static com.exe.sharkauction.mappers.IBrandMapper.INSTANCE;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/brand")
@RequiredArgsConstructor
public class BrandController {
    private final IBrandService brandService;
    @PostMapping("")
    public CoreApiResponse<BrandEntity> createBrand(
            @Valid @RequestBody BrandRequest brandRequest
    ){
        BrandEntity brandEntity = brandService.createBrand(INSTANCE.toModel(brandRequest));
        return CoreApiResponse.success(brandEntity,"Insert brand successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<BrandEntity>> getAllBrands(){
        List<BrandEntity> brand = brandService.getAllBrands();
        return CoreApiResponse.success(brand);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<BrandEntity> getBrandById(@Valid @PathVariable Long id){
        BrandEntity brand = brandService.getBrandById(id);
        return CoreApiResponse.success(brand);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<BrandEntity> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest brandRequest
    ){
        BrandEntity updateBrand = brandService.updateBrand(id, INSTANCE.toModel(brandRequest));
        return CoreApiResponse.success(updateBrand, "Update brand successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteBrand(
            @PathVariable Long id
    ){
        brandService.deleteBrand(id);
        return CoreApiResponse.success("Delete brand successfully");
    }
}