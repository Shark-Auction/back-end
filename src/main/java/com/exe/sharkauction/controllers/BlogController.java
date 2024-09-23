package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.BlogEntity;
import com.exe.sharkauction.requests.BlogRequest;
import com.exe.sharkauction.services.IBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/blog")
@RequiredArgsConstructor
public class BlogController {
    private final IBlogService blogService;

    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @PostMapping("")
    public CoreApiResponse<BlogEntity> createBlog(
            @ModelAttribute BlogRequest blogRequest,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {

        BlogEntity createdBlog = blogService.createBlog(blogRequest, images);
        return  CoreApiResponse.success(createdBlog);
    }

    @GetMapping("/all")
    public CoreApiResponse<List<BlogEntity>> getAllBlogs() {
        List<BlogEntity> blogs = blogService.getAllBlogs();
        return CoreApiResponse.success(blogs);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<BlogEntity> getBlogById(@PathVariable Long id) {
        BlogEntity blog = blogService.getBlogById(id);
        return CoreApiResponse.success(blog) ;
    }

    @PreAuthorize("hasAnyRole('MANAGER','STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return CoreApiResponse.success("Delete blog successfully") ;
    }
}
