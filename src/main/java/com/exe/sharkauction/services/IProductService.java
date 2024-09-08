package com.exe.sharkauction.services;

import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.requests.ProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    ProductEntity createProduct(ProductEntity product, MultipartFile imageFile, List<MultipartFile> images) throws IOException;
    ProductEntity getProductById(long id);
    List<ProductEntity> getAllProducts();
    ProductEntity updateProduct(long productId, ProductRequest product);
    void deleteProduct(long id);
    void uploadThumbnail(Long productId, MultipartFile imageFile) throws IOException;
    List<ProductEntity> getMyProduct();


}
