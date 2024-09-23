package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.constants.ImageContants;
import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.securities.UserPrincipal;
import com.exe.sharkauction.components.utils.UploadImagesUtils;
import com.exe.sharkauction.models.BlogEntity;
import com.exe.sharkauction.models.BlogImageEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.repositories.IBlogImageRepository;
import com.exe.sharkauction.repositories.IBlogRepository;
import com.exe.sharkauction.requests.BlogRequest;
import com.exe.sharkauction.services.IBlogService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class BlogService implements IBlogService {
    private final IBlogRepository blogRepository;

    private final IBlogImageRepository blogImageRepository;

    @Override
    public BlogEntity createBlog(BlogRequest blogRequest, List<MultipartFile> images) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        BlogEntity blog = new BlogEntity();
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setUser(user);

        BlogEntity savedBlog = blogRepository.save(blog);

        if (images != null && !images.isEmpty()) {
            List<BlogImageEntity> blogImageList = new ArrayList<>();
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    BlogImageEntity blogImage = new BlogImageEntity();
                    blogImage.setBlog(savedBlog);
                    blogImage.setUrl(UploadImagesUtils.storeFile(image, ImageContants.BLOG_IMAGE_PATH));
                    blogImageList.add(blogImageRepository.save(blogImage));
                }
            }
            savedBlog.setBlogImages(blogImageList);
        }

        return savedBlog;
    }

    @Override
    public List<BlogEntity> getAllBlogs() {
        return blogRepository.findAll();
    }

    @Override
    public void deleteBlog(Long blogId) {
        blogRepository.deleteById(blogId);
    }

    @Override
    public BlogEntity getBlogById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This blog is not existed!"));
    }
}