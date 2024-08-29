package com.exe.sharkauction.services;

import com.exe.sharkauction.models.CategoryEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ICategoryService {

    CategoryEntity createCategory(CategoryEntity category, MultipartFile imageFile) throws IOException;
    CategoryEntity   getCategoryById(long id);
    List<CategoryEntity> getAllCategories();
    CategoryEntity updateCategory(long categoryId, CategoryEntity category, MultipartFile imageFile)throws IOException;
    void deleteCategory(long id);
}
