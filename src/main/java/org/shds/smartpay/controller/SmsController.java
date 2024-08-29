package org.shds.smartpay.controller;

import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.shds.smartpay.service.CoolSmsServiceImpl;
import org.shds.smartpay.service.VerificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private CoolSmsServiceImpl coolSmsService;

    @Autowired
    private VerificationServiceImpl verificationService;

    @PostMapping("/send")
    public String sendSms(@RequestBody Map<String, String> body) {
        String phoneNumber = body.get("phoneNumber");
        try {
            String generatedCode = coolSmsService.sendSms(phoneNumber);
            return "생성된 인증코드: " + generatedCode;
        } catch (CoolsmsException e) {
            e.printStackTrace();
            return "Failed to send SMS: " + e.getMessage();
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> body) {
        String phoneNumber = body.get("phoneNumber");
        String code = body.get("code");

        if (verificationService.verifyCode(phoneNumber, code)) {
            return ResponseEntity.ok("문자인증 성공");
        } else {
            return ResponseEntity.status(401).body("문자인증 실패");
        }
    }

    @GetMapping("/checkRedis")
    public String checkRedisConnection() {
        return verificationService.checkRedisConnection();
    }
}
