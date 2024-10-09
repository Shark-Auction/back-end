package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.CashOutStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cash_outs")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashOutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity user;

    private float money;

    @JoinColumn(name = "bank_account_number")
    private String bankAccountNumber;

    @JoinColumn(name = "bank_account_name")
    private String bankAccountName;

    @Enumerated(EnumType.STRING)
    private CashOutStatus status;


}
