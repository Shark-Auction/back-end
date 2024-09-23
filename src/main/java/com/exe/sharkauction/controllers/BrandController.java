package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.BrandEntity;
import com.exe.sharkauction.requests.BrandRequest;
import com.exe.sharkauction.services.IBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.exe.sharkauction.mappers.IBrandMapper.INSTANCE;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/brand")
@RequiredArgsConstructor
public class BrandController {
    private final IBrandService brandService;
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @PostMapping("")
    public CoreApiResponse<BrandEntity> createBrand(
            @Valid @RequestBody BrandRequest brandRequest
    ){
        BrandEntity brandEntity = brandService.createBrand(INSTANCE.toModel(brandRequest));
        return CoreApiResponse.success(brandEntity,"Thêm nhãn hàng mới thành công");
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

    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @PutMapping("/{id}")
    public CoreApiResponse<BrandEntity> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest brandRequest
    ){
        BrandEntity updateBrand = brandService.updateBrand(id, INSTANCE.toModel(brandRequest));
        return CoreApiResponse.success(updateBrand, "Cập nhật nhãn hàng thành công");
    }

    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteBrand(
            @PathVariable Long id
    ){
        brandService.deleteBrand(id);
        return CoreApiResponse.success("Xoá nhãn hàng thành công");
    }
}
