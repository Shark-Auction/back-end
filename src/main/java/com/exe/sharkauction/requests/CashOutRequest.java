package com.exe.sharkauction.requests;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CashOutRequest {

    private String bankAccountName;

    private String bankAccountNumber;

}
