package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.ViolateEntity;
import com.exe.sharkauction.requests.ViolateRequest;
import com.exe.sharkauction.services.IViolateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.exe.sharkauction.mappers.IViolateMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/violate")
@RequiredArgsConstructor
public class ViolateController {
    private final IViolateService violateService;

    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @PostMapping("")
    public CoreApiResponse<ViolateEntity> createViolate(
            @Valid @RequestBody ViolateRequest request
    ){
        ViolateEntity violate = violateService.createViolate(INSTANCE.toModel(request));
        return CoreApiResponse.success(violate,"Thêm vi phạm người dùng thành công");
    }
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @GetMapping("")
    public CoreApiResponse<List<ViolateEntity>> getAllViolates(){
        List<ViolateEntity> violates = violateService.getAllViolate();
        return CoreApiResponse.success(violates);
    }
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @GetMapping("/{id}")
    public CoreApiResponse<ViolateEntity> getViolateById(@Valid @PathVariable Long id){
        ViolateEntity violate = violateService.getViolateById(id);
        return CoreApiResponse.success(violate);
    }

    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @GetMapping("/user/{userId}")
    public CoreApiResponse<List<ViolateEntity>> getViolateByUser(long userId){
        List<ViolateEntity> violates = violateService.getViolateByUserId(userId);
        return CoreApiResponse.success(violates);
    }

    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteViolate(@PathVariable Long id) {
        violateService.deleteViolate(id);
        return CoreApiResponse.success("Xoá vi phạm thành công") ;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/myviolate")
    public CoreApiResponse<List<ViolateEntity>> getMyViolate(){
        List<ViolateEntity> violates = violateService.getViolateMyViolate();
        return CoreApiResponse.success(violates);
    }

}
