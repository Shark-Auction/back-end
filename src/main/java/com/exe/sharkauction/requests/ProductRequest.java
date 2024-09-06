package com.exe.sharkauction.requests;

import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String name;

    private String description;

    private Long categoryId;

    private String brandName;

    private String originName;

    private float startingPrice;

    private String condition;


}
