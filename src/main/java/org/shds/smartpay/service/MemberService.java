package org.shds.smartpay.service;


import org.shds.smartpay.entity.Member;
import org.shds.smartpay.security.dto.MemberRegisterDTO;

public interface MemberService {
    Member registerNewMember(MemberRegisterDTO memberRegisterDTO) throws Exception;
    Member findByEmail(String email);
    void updateMember(Member member);
    Member findByMemberNo(String memberNo);
    boolean verifyPayPwd(String memberNo, String payPwd);
    boolean isPaypwdEmpty(String memberNo);
    boolean isEmailDuplicate(String email);
    Member findByPhone(String phone);
}
