package com.exe.sharkauction.requests;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {
    private Long customerId;
    private Long productId;
    private float ratingValue;
    private String review;
}
