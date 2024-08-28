package org.shds.smartpay.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shds.smartpay.entity.MemberRole;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDTO {
    private String member_no;
    private String email;
    private String name;
    private String phone;
    private boolean from_social;
    private String regUser;
    private String payPwd; //컨트롤러에서 일단 안받아오는 상태임
    private String socialId;
    private MemberRole role;
}
