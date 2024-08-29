package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.constants.ImageContants;
import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.utils.StringUtils;
import com.exe.sharkauction.components.utils.UploadImagesUtils;
import com.exe.sharkauction.models.CategoryEntity;
import com.exe.sharkauction.repositories.ICategoryRepository;
import com.exe.sharkauction.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    public CategoryEntity createCategory(CategoryEntity category, MultipartFile imageFile) throws IOException {
        category.setName(StringUtils.NameStandardlizing(category.getName()));

        if (category.getParent() != null && category.getParent().getId() != 0) {
            Long parentId = category.getParent().getId();
            CategoryEntity existingCategory = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This category does not exist!"));
            category.setParent(existingCategory);
        } else {
            category.setParent(null);
        }

        if(categoryRepository.existsByName(category.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "That name is already existed!");
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            category.setImageUrl(UploadImagesUtils.storeFile(imageFile, ImageContants.CATEGORY_IMAGE_PATH));
        }

        return categoryRepository.save(category);
    }

    @Override
    public CategoryEntity getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This categod is not existed!"));
    }
    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity updateCategory(long categoryId, CategoryEntity category, MultipartFile imageFile) throws IOException {
        category.setName(StringUtils.NameStandardlizing(category.getName()));
        if (categoryRepository.existsByName(category.getName())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "That name is existed!");
        }

        CategoryEntity existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));

        existingCategory.setName(category.getName());

        if (imageFile != null && !imageFile.isEmpty()) {

            String imageUrl = UploadImagesUtils.storeFile(imageFile, ImageContants.CATEGORY_IMAGE_PATH);
            existingCategory.setImageUrl(imageUrl);
        }

        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(long id) {
        CategoryEntity existingCategory = this.getCategoryById(id);
        categoryRepository.delete(existingCategory);
    }
}
