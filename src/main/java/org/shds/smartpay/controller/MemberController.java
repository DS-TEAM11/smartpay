package org.shds.smartpay.controller;

import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.entity.Member;
import org.shds.smartpay.security.dto.MemberRegisterDTO;
import org.shds.smartpay.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
