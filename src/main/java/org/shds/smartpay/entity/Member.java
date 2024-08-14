package org.shds.smartpay.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{
    @Id
    private String memberNo;

    private String email;
    private String pwd;
    private String name;
    private String phone;

    private boolean fromSocial;
    private String regUser;
    private String payPwd;

}