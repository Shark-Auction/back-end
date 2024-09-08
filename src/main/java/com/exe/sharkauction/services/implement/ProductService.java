package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.constants.ImageContants;
import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.exceptions.DataNotFoundException;
import com.exe.sharkauction.components.securities.UserPrincipal;
import com.exe.sharkauction.components.utils.StringUtils;
import com.exe.sharkauction.components.utils.UploadImagesUtils;
import com.exe.sharkauction.mappers.IProductMapper;
import com.exe.sharkauction.models.*;
import com.exe.sharkauction.models.enums.ProductStatus;
import com.exe.sharkauction.repositories.IBrandRepository;
import com.exe.sharkauction.repositories.ICategoryRepository;
import com.exe.sharkauction.repositories.IOriginRepository;
import com.exe.sharkauction.repositories.IProductRepository;
import com.exe.sharkauction.requests.ProductRequest;
import com.exe.sharkauction.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final IBrandRepository brandRepository;
    private final IOriginRepository originRepository;
    private final IProductMapper productMapper;

    @Override
    public ProductEntity createProduct(ProductEntity product, MultipartFile imageFile, List<MultipartFile> images) throws IOException {
        CategoryEntity existingCategory = categoryRepository
                .findById(product.getCategory().getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Category", "id", product.getCategory().getId()));

        BrandEntity existingBrand = brandRepository
                .findByName(product.getBrand().getName());

        if (existingBrand == null) {
            throw new DataNotFoundException(
                    "Brand", "name", product.getBrand().getName());
        }
        OriginEntity existingOrigin = originRepository
                .findByName(product.getOrigin().getName());

        if (existingOrigin == null) {
            throw new DataNotFoundException(
                    "Origin", "name", product.getOrigin().getName());
        }


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        product.setName(StringUtils.NameStandardlizing(product.getName()));
        product.setCategory(existingCategory);
        product.setBrand(existingBrand);
        product.setOrigin(existingOrigin);
        product.setSeller(user);
        product.setStatus(ProductStatus.PENDING);

        if (imageFile != null && !imageFile.isEmpty()) {
            product.setThumbnail(UploadImagesUtils.storeFile(imageFile, ImageContants.PRODUCT_IMAGE_PATH));
        }

        if (images != null && !images.isEmpty()) {
            List<ProductImageEntity> productImageList = new ArrayList<>();
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    ProductImageEntity productImage = new ProductImageEntity();
                    productImage.setJewelry(product);
                    productImage.setUrl(UploadImagesUtils.storeFile(image, ImageContants.PRODUCT_IMAGE_PATH));
                    productImageList.add(productImage);
                }
            }
            product.setProduct_images(productImageList);
        }


        return productRepository.save(product);
    }

    @Override
    public ProductEntity getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find jewelry with id: " + id));
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }









    @Override
    public ProductEntity updateProduct(long productId, ProductRequest request) {
        ProductEntity existingProduct = getProductById(productId);
        productMapper.updateProductFromRequest(request, existingProduct);

        if (request.getCategoryId() != null && !existingProduct.getCategory().getId().equals(request.getCategoryId())) {
            CategoryEntity existingCategory = categoryRepository
                    .findById(request.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Category", "id", request.getCategoryId()));
            existingProduct.setCategory(existingCategory);
        }

        if (request.getBrandName() != null && !existingProduct.getBrand().getName().equals(request.getBrandName())) {
            BrandEntity existingBrand = brandRepository
                    .findByName(request.getBrandName());
            existingProduct.setBrand(existingBrand);
}

        if (request.getOriginName() != null && !existingProduct.getOrigin().getName().equals(request.getOriginName())) {
            OriginEntity existingOrigin = originRepository
                    .findByName(request.getOriginName());
            existingProduct.setOrigin(existingOrigin);
        }

        return productRepository.save(existingProduct);
    }

    @Override
    public void uploadThumbnail(Long productId, MultipartFile imageFile) throws IOException {
        ProductEntity existingProduct = getProductById(productId);
        existingProduct.setThumbnail(UploadImagesUtils.storeFile(imageFile, ImageContants.PRODUCT_IMAGE_PATH));
        productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(long id) {
        ProductEntity existingProduct = getProductById(id);

        if (existingProduct.getStatus() == ProductStatus.PENDING) {
            productRepository.deleteById(id);
        } else {
            throw new AppException(HttpStatus.BAD_REQUEST,"Sản phẩm đang trong quá trình đấu giá không thể xoá");
        }
    }

    @Override
    public List<ProductEntity> getMyProduct() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return productRepository.findProductsBySeller(user);
    }

}
