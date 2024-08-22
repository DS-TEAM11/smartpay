package org.shds.smartpay.controller;

import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.entity.Member;
import org.shds.smartpay.security.dto.MemberRegisterDTO;
import org.shds.smartpay.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequestMapping("/member/")
@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"})
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Member> registerMember(@RequestBody MemberRegisterDTO memberRegisterDTO) throws Exception {
        Member registeredMember = memberService.registerNewMember(memberRegisterDTO);
        log.info("registerMember 진입");
        return ResponseEntity.ok(registeredMember);
    }

    @GetMapping("/findMember")
    public ResponseEntity<String> findMemberNo(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();  // JWT에서 이메일(사용자 이름)을 가져옴
        Member member = memberService.findByEmail(email);
        if (member != null) {
            return ResponseEntity.ok(String.valueOf(member.getMemberNo()));  // memberNo 반환
        } else {
            return ResponseEntity.notFound().build();  // 해당 사용자가 없는 경우 404 응답
        }
    }

    @GetMapping("/jwt-test")
    public ResponseEntity<String> jwtTest() {
        return ResponseEntity.ok("jwtTest 요청 성공");
    }

    @GetMapping("/all")
    public void exAll() {
        log.info("exAll........");
    }

    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal MemberRegisterDTO memberDTO) {

        log.info("exMember........");

        log.info("clubAuthMemberDTO : "+memberDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void exAdmin() {
        log.info("exAdmin........");
    }
}
