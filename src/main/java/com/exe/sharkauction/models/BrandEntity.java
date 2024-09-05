package com.exe.sharkauction.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brands")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
