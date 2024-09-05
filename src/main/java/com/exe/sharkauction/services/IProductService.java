package com.exe.sharkauction.services;

import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.requests.ProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    ProductEntity createProduct(ProductEntity jewelry, MultipartFile imageFile, List<MultipartFile> images) throws IOException;
    ProductEntity getProductById(long id);
    List<ProductEntity> getAllJewelrys();
    ProductEntity updateProduct(long jewelryId, ProductRequest product);
    void deleteProduct(long id);
    void uploadThumbnail(Long jewelryId, MultipartFile imageFile) throws IOException;

    List<ProductEntity> getJewelryBySellerId();


}
