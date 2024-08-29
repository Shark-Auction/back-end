package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.CategoryEntity;
import com.exe.sharkauction.requests.CategoryRequest;
import com.exe.sharkauction.services.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.exe.sharkauction.mappers.ICategoryMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryServices;

    @PostMapping("")
    public CoreApiResponse<CategoryEntity> createCategory(
            @Valid @ModelAttribute CategoryRequest categoryRequest,
            @RequestParam(name = "imageThumbnail", required = false) MultipartFile imageThumbnail
    ) throws IOException {
        CategoryEntity categoryResponse = categoryServices.createCategory(INSTANCE.toModel(categoryRequest),imageThumbnail);
        return CoreApiResponse.success(categoryResponse,"Insert category successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<CategoryEntity>> getAllCategories(){
        List<CategoryEntity> material = categoryServices.getAllCategories();
        return CoreApiResponse.success(material);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<CategoryEntity> getCategoryById(@Valid @PathVariable Long id){
        CategoryEntity material = categoryServices.getCategoryById(id);
        return CoreApiResponse.success(material);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<CategoryEntity> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest,
            @RequestParam(name = "imageThumbnail", required = false) MultipartFile imageThumbnail

    ) throws IOException {
        CategoryEntity updateCategory = categoryServices.updateCategory(id, INSTANCE.toModel(categoryRequest),imageThumbnail);
        return CoreApiResponse.success(updateCategory, "Update category successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteCategory(
            @PathVariable Long id
    ){
        categoryServices.deleteCategory(id);
        return CoreApiResponse.success("Delete category successfully");
    }

}
