package com.exe.sharkauction.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "origins")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OriginEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
