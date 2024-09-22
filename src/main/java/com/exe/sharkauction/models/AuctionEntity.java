package com.exe.sharkauction.models;

import com.exe.sharkauction.models.enums.AuctionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
@Entity
@Table(name = "auctions")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Date startTime;

    private Date endTime;

    private float step;

    private int totalBids;

    private float currentPrice;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private UserEntity winner;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;
}
