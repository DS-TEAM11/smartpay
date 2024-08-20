package org.shds.smartpay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class BinTable{

    @Id
    @GeneratedValue
    private int binNo;

    private String argName;
    private String bin;
    private int isCredit;
}
