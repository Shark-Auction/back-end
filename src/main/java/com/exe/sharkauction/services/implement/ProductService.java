package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.models.CategoryEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.repositories.ICategoryRepository;
import com.exe.sharkauction.repositories.IProductRepository;
import com.exe.sharkauction.services.IProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {//implements IProductService {
//    private final IProductRepository jewelryRepository;
//    private final ICategoryRepository categoryRepository;
//    private final IBrandRepository brandRepository;
//    private final ICollectionRepository collectionRepository;
//    private final IMaterialRepository materialRepository;
//    private final IDeliveryMethodRepository deliveryMethodRepository;
//    private final JewelryMapper jewelryMapper;
//    @Override
//    public ProductEntity createJewelry(ProductEntity jewelry, MultipartFile imageFile, List<MultipartFile> images) throws IOException {
//        CategoryEntity existingCategory = categoryRepository
//                .findById(jewelry.getCategory().getId())
//                .orElseThrow(() ->
//                        new DataNotFoundException(
//                                "Category", "id", jewelry.getCategory().getId()));
//
//        BrandEntity existingBrand = brandRepository
//                .findByName(jewelry.getBrand().getName());
//
//
//        CollectionEntity existingCollection = collectionRepository
//                .findByName(jewelry.getCollection().getName());
//
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//        UserEntity user = userPrincipal.getUser();
//
//        jewelry.setName(StringUtils.NameStandardlizing(jewelry.getName()));
//        jewelry.setCategory(existingCategory);
//        jewelry.setBrand(existingBrand);
//        jewelry.setCollection(existingCollection);
//        jewelry.setSellerId(user);
//        jewelry.setStatus(JewelryStatus.PENDING);
//
//        if (imageFile != null && !imageFile.isEmpty()) {
//            jewelry.setThumbnail(UploadImagesUtils.storeFile(imageFile, ImageContants.JEWELRY_IMAGE_PATH));
//        }
//
//        if (images != null && !images.isEmpty()) {
//            List<JewelryImageEntity> birdImagesList = new ArrayList<>();
//            for (MultipartFile image : images) {
//                if (!image.isEmpty()) {
//                    JewelryImageEntity jewelryImage = new JewelryImageEntity();
//                    jewelryImage.setJewelry(jewelry);
//                    jewelryImage.setUrl(UploadImagesUtils.storeFile(image, ImageContants.JEWELRY_IMAGE_PATH));
//                    birdImagesList.add(jewelryImage);
//                }
//            }
//            jewelry.setJewelryImages(birdImagesList);
//        }
//        List<JewelryMaterialEntity> jewelryMaterialList = jewelry.getJewelryMaterials();
//        List<JewelryMaterialEntity> newJewelryMaterialList = new ArrayList<>();
//        for (JewelryMaterialEntity material : jewelryMaterialList) {
//            JewelryMaterialEntity jewelryMaterial = new JewelryMaterialEntity();
//            jewelryMaterial.setJewelry(jewelry);
//            jewelryMaterial.setWeight(material.getWeight());
//            MaterialEntity materialEntity = materialRepository.findById(material.getMaterial().getId())
//                    .orElseThrow(() -> new DataNotFoundException("Category", "id", material.getMaterial().getId()));
//            jewelryMaterial.setMaterial(materialEntity);
//            newJewelryMaterialList.add(jewelryMaterial);
//        }
//        jewelry.setJewelryMaterials(newJewelryMaterialList);
//        JewelryEntity savedJewelry = jewelryRepository.save(jewelry);
//
//        savedJewelry.getJewelryMaterials().forEach(jm -> {
//            MaterialEntity fullMaterial = materialRepository.findById(jm.getMaterial().getId()).orElse(null);
//            jm.setMaterial(fullMaterial);
//        });
//
//        return savedJewelry;
//    }
}
