package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.constants.ImageContants;
import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.utils.StringUtils;
import com.exe.sharkauction.components.utils.UploadImagesUtils;
import com.exe.sharkauction.models.CategoryEntity;
import com.exe.sharkauction.repositories.ICategoryRepository;
import com.exe.sharkauction.requests.CategoryRequest;
import com.exe.sharkauction.responses.CategoryResponse;
import com.exe.sharkauction.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Override
    public CategoryResponse getCategoryHierarchy(Long categoryId) {
        CategoryEntity targetCategory = categoryRepository.findById(categoryId).orElse(null);
        if (targetCategory == null) {
            return null; // Không tìm thấy danh mục
        }

        CategoryEntity rootCategory = findRootCategory(targetCategory); // Tìm danh mục gốc
        return buildCategoryTree(rootCategory, targetCategory); // Xây dựng cây danh mục từ gốc
    }

    // Tìm danh mục gốc từ một danh mục bất kỳ
    private CategoryEntity findRootCategory(CategoryEntity category) {
        while (category.getParent() != null) {
            category = category.getParent();
        }
        return category;
    }

    // Xây dựng cây danh mục từ gốc, chỉ giữ lại các nhánh cần thiết
    private CategoryResponse buildCategoryTree(CategoryEntity currentCategory, CategoryEntity targetCategory) {
        if (currentCategory == null) {
            return null;
        }

        // Tạo danh sách con đệ quy
        List<CategoryResponse> childrenDtos = new ArrayList<>();
        List<CategoryEntity> children = categoryRepository.findByParent(currentCategory);

        for (CategoryEntity child : children) {
            // Chỉ thêm nhánh con nếu nó nằm trên đường dẫn đến danh mục mục tiêu
            if (isAncestor(child, targetCategory)) {
                childrenDtos.add(buildCategoryTree(child, targetCategory));
            }
        }

        // Trả về DTO của danh mục hiện tại với các con đã lọc
        return new CategoryResponse(
                currentCategory.getId(),
                currentCategory.getName(),
                currentCategory.getImageUrl(),
                childrenDtos
        );
    }

    private boolean isAncestor(CategoryEntity ancestor, CategoryEntity target) {
        while (target != null) {
            if (target.equals(ancestor)) {
                return true;
            }
            target = target.getParent();
        }
        return false;
    }

    @Override
    public List<CategoryResponse> getChildCategory(Long parentId) {
        CategoryEntity parentCategory = categoryRepository.findById(parentId).orElse(null);
        if (parentCategory == null) {
            return List.of(); // Trả về danh sách rỗng nếu không tìm thấy danh mục cha
        }

        // Lấy danh sách các danh mục con và chuyển đổi sang DTO
        List<CategoryEntity> childCategories = categoryRepository.findByParent(parentCategory);
        return childCategories.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Chuyển đổi CategoryEntity sang CategoryDTO
    private CategoryResponse convertToDto(CategoryEntity category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getImageUrl(),
                null
        );
    }


}
