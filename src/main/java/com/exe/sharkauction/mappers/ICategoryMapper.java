package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.CategoryEntity;
import com.exe.sharkauction.requests.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {
    ICategoryMapper INSTANCE = Mappers.getMapper(ICategoryMapper.class);
    @Mapping(target = "parent", source = "parent_id", qualifiedByName = "idToParentCategory")
    CategoryEntity toModel(CategoryRequest request);

    @Named("idToParentCategory")
    default CategoryEntity idToParentCategory(Long id) {
        if (id == null) {
            return null;
        }
        CategoryEntity category = new CategoryEntity();
        category.setId(id);
        return category;
    }

}
