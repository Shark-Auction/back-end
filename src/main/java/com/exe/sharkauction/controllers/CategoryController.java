package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.CategoryEntity;
import com.exe.sharkauction.requests.CategoryRequest;
import com.exe.sharkauction.responses.CategoryResponse;
import com.exe.sharkauction.services.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @PostMapping("")
    public CoreApiResponse<CategoryEntity> createCategory(
            @Valid @ModelAttribute CategoryRequest categoryRequest,
            @RequestParam(name = "imageThumbnail", required = false) MultipartFile imageThumbnail
    ) throws IOException {
        CategoryEntity categoryResponse = categoryServices.createCategory(INSTANCE.toModel(categoryRequest),imageThumbnail);
        return CoreApiResponse.success(categoryResponse,"Thêm danh mục mới thành công");
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
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @PutMapping("/{id}")
    public CoreApiResponse<CategoryEntity> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest,
            @RequestParam(name = "imageThumbnail", required = false) MultipartFile imageThumbnail

    ) throws IOException {
        CategoryEntity updateCategory = categoryServices.updateCategory(id, INSTANCE.toModel(categoryRequest),imageThumbnail);
        return CoreApiResponse.success(updateCategory, "Cập nhật danh mục thành công");
    }
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteCategory(
            @PathVariable Long id
    ){
        categoryServices.deleteCategory(id);
        return CoreApiResponse.success("Xoá danh mục thành công");
    }


    @GetMapping("/parents/{categoryId}")
    public CategoryResponse getParentCategories(@PathVariable Long categoryId) {
        return categoryServices.getCategoryHierarchy(categoryId);
    }

    @GetMapping("/children/{parentId}")
    public List<CategoryResponse> getChildCategories(@PathVariable Long parentId) {
        return categoryServices.getChildCategory(parentId);
    }



}
