package org.shds.smartpay.service;


import org.shds.smartpay.entity.Member;
import org.shds.smartpay.security.dto.MemberRegisterDTO;

public interface MemberService {
    Member registerNewMember(MemberRegisterDTO memberRegisterDTO) throws Exception;
}
