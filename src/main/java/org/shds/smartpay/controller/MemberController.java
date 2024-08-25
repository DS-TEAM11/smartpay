package org.shds.smartpay.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.entity.Member;
import org.shds.smartpay.security.dto.MemberRegisterDTO;
import org.shds.smartpay.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@Log4j2
@RequestMapping("/member/")
@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"})
public class MemberController {

    @Autowired
    private MemberService memberService;

    public static class PayPwdRequest {
        @Pattern(regexp = "^\\d{6}$", message = "비밀번호는 6자리 숫자여야 합니다.")
        private String payPwd;
        private String memberNo;

        public String getPayPwd() {
            return payPwd;
        }

        public String getMemberNo() {
            return memberNo;
        }

        public void setPayPwd(String payPwd) {
            this.payPwd = payPwd;
        }
    }

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

    @PostMapping("/setPaypwd")
    public ResponseEntity<String> setPaypwd(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PayPwdRequest request) {
        //String email = userDetails.getUsername();
        Member member = memberService.findByMemberNo(request.getMemberNo());
        if (member != null) {
            member.setPayPwd(request.getPayPwd());
            memberService.updateMember(member);
            return ResponseEntity.ok(String.valueOf(member.getMemberNo()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
        }
    }

    @PostMapping("/isTruePaypwd")
    public ResponseEntity<Void> isTruePaypwd(@Valid @RequestBody PayPwdRequest request) {
        boolean isTrue = memberService.verifyPayPwd(request.getMemberNo(), request.getPayPwd());
        if (isTrue) {
            return ResponseEntity.ok().build();  // 비밀번호 일치 시 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 비밀번호 불일치 시 404 Not Found
        }
    }


    @GetMapping("/checkSms")
    @ResponseBody
    public String sendVerificationCode(@RequestParam String phone) {
        // 4자리 랜덤 숫자 생성
        String verificationCode = generateVerificationCode();

        // 콘솔 및 로그 출력
        System.out.println("Generated Verification Code: " + verificationCode);

        // CoolSMS API를 사용한 SMS 전송은 주석 처리
        /*
        Message message = new Message();
        message.setFrom("발신번호");
        message.setTo(phone);
        message.setText("[YourApp] 인증번호: " + verificationCode);

        try {
            messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception e) {
            System.out.println("SMS 전송 실패: " + e.getMessage());
        }
        */

        return verificationCode;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(8999) + 1000;
        return String.valueOf(code);
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
