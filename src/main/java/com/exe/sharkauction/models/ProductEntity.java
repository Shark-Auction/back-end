package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.DeliveryMethod;
import com.exe.sharkauction.models.enums.ProductCondition;
import com.exe.sharkauction.models.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity seller;

    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @ManyToOne
    @JoinColumn(name = "origin_id")
    private OriginEntity origin;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_condition")
    private ProductCondition condition;

    private float startingPrice;

    private float buyNowPrice;

    private float finalPrice;

    private float desiredPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus status;

    @OneToMany(mappedBy = "jewelry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImageEntity> product_images;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    private boolean buyNow;
}