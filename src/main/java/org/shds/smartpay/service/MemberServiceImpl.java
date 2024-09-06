package org.shds.smartpay.service;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.shds.smartpay.entity.Member;
import org.shds.smartpay.entity.MemberRole;
import org.shds.smartpay.repository.MemberRepository;
import org.shds.smartpay.security.dto.MemberRegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Member findByEmail(String email){
        return memberRepository.findByEmail(email).get();
    }

    @Override
    public Member findByMemberNo(String memberNo){
        return memberRepository.findByMemberNo(memberNo).get();
    }

    @Override
    public void updateMember(Member member){
        memberRepository.save(member);
    }

    @Override
    public boolean verifyPayPwd(String memberNo, String payPwd) {
        Optional<Member> memberOptional = memberRepository.findByMemberNo(memberNo);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return passwordEncoder.matches(payPwd, member.getPayPwd());
            //return member.getPayPwd().equals(payPwd);  // 비밀번호가 일치하는지 확인
        }
        return false;  // 회원이 없거나 비밀번호가 일치하지 않으면 false 반환
    }

    @Override
    public boolean isPaypwdEmpty(String memberNo) {
        return memberRepository.findByMemberNo(memberNo)
                .map(member -> member.getPayPwd() == null || member.getPayPwd().isEmpty())
                .orElse(false); // 멤버가 존재하지 않으면 false 반환
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Override
    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone).orElse(null);
    }

    @Override
    public Member registerNewMember(MemberRegisterDTO memberRegisterDTO) throws Exception {
        if (memberRepository.findByEmail(memberRegisterDTO.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        Member member = Member.builder()
                .memberNo(UUID.randomUUID().toString())
                .email(memberRegisterDTO.getEmail())
                .password(passwordEncoder.encode(memberRegisterDTO.getPassword()))  // 비밀번호 암호화
                .name(memberRegisterDTO.getName())
                .phone(memberRegisterDTO.getPhone())
                .fromSocial(false)
                .regUser("Developer Test")
                .role(MemberRole.USER)
                .build();

        return memberRepository.save(member);
    }
}
