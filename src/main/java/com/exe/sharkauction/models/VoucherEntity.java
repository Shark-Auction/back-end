package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.VoucherStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "vouchers")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    
    private String voucherCode;

    private float discount;

    private Date startTime;

    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @Enumerated(EnumType.STRING)
    private VoucherStatus status;

}
