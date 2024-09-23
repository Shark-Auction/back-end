package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.ViolateStatus;
import com.exe.sharkauction.models.enums.ViolateType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "violates")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViolateEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ViolateType type;

    private String description;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ViolateStatus status;


}
