package org.shds.smartpay.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BinTableDTO {

    private int binNo;
    private String argName;
    private String bin;
    private int isCredit;

}
