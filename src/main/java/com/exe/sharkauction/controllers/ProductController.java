package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.requests.ProductRequest;
import com.exe.sharkauction.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import static com.exe.sharkauction.mappers.IProductMapper.INSTANCE;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/product")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public CoreApiResponse<ProductEntity> createProduct(
            @Valid @ModelAttribute ProductRequest request,
            @RequestParam(name = "imageThumbnail", required = false) MultipartFile imageThumbnail,
            @RequestParam(name = "imagesFile", required = false) List<MultipartFile> images
    ) throws IOException {
        ProductEntity product = productService.createProduct(INSTANCE.toModel(request),imageThumbnail,images);
        return CoreApiResponse.success(product,"Insert product successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<ProductEntity>> getAllProducts(){
        List<ProductEntity> products = productService.getAllProducts();
        return CoreApiResponse.success(products);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<ProductEntity> getProductById(@Valid @PathVariable Long id){
        ProductEntity product = productService.getProductById(id);
        return CoreApiResponse.success(product);
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteProduct(
            @PathVariable Long id
    ){
        productService.deleteProduct(id);
        return CoreApiResponse.success("Delete product successfully");
    }

    @PutMapping("confirm/{id}")
    public CoreApiResponse<?> confirmProduct(
            @PathVariable Long id
    ){
        productService.confirmProduct(id);
        return CoreApiResponse.success("Confirm product successfully");
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public List<ProductEntity> getJewelryBySellerId() {
        return productService.getMyProduct();
    }


    @PutMapping("/{id}")
    public CoreApiResponse<ProductEntity> updateMaterial(
            @PathVariable Long id,
            @RequestBody ProductRequest productRequest
    ){
        ProductEntity updateJewelry= productService.updateProduct(id, productRequest);
        return CoreApiResponse.success(updateJewelry, "Update product successfully");
    }





}
