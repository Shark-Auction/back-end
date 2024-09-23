package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.OriginEntity;
import com.exe.sharkauction.requests.OriginRequest;
import com.exe.sharkauction.services.IOriginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.exe.sharkauction.mappers.IOriginMapper.INSTANCE;
@RestController
@RequestMapping("${app.api.version.v1}/origin")
@RequiredArgsConstructor
public class OriginController {
    private final IOriginService originService;
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @PostMapping("")
    public CoreApiResponse<OriginEntity> createOrigin(
            @Valid @RequestBody OriginRequest originRequest
    ){
        OriginEntity originEntity = originService.createOrigin(INSTANCE.toModel(originRequest));
        return CoreApiResponse.success(originEntity,"Thêm nguồn gốc mới thành công");
    }

    @GetMapping("")
    public CoreApiResponse<List<OriginEntity>> getAllOrigins(){
        List<OriginEntity> origins = originService.getAllOrigins();
        return CoreApiResponse.success(origins);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<OriginEntity> getOriginById(@Valid @PathVariable Long id){
        OriginEntity origin = originService.getOriginById(id);
        return CoreApiResponse.success(origin);
    }
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @PutMapping("/{id}")
    public CoreApiResponse<OriginEntity> updateOrigin(
            @PathVariable Long id,
            @Valid @RequestBody OriginRequest originRequest
    ){
        OriginEntity updateOrigin = originService.updateOrigin(id, INSTANCE.toModel(originRequest));
        return CoreApiResponse.success(updateOrigin, "Cập nhật nguồn gốc thành công");
    }
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteOrigin(
            @PathVariable Long id
    ){
        originService.deleteOrigin(id);
        return CoreApiResponse.success("Xoá nguồn gốc thành công");
    }
}
