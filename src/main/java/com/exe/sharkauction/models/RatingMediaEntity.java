package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "rating_medias")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingMediaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// Generates a random UUID

    @ManyToOne
    @JoinColumn(name = "rating_id", nullable = false)
    private RatingEntity ratingEntity;

    @Column(nullable = false)
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType;
}
