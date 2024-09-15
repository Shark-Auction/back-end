package com.exe.sharkauction.requests;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingMediaRequest {
    private MultipartFile file;
}
