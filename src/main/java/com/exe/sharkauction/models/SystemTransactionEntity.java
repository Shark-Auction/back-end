package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "system_transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemTransactionEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float money;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
