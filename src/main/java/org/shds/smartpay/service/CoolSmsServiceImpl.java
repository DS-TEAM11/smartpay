package org.shds.smartpay.service;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
@Slf4j
@Service
public class CoolSmsServiceImpl {
    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    @Autowired
    private VerificationServiceImpl verificationService;


    public String sendSms(String to) throws CoolsmsException {
        try {
            // 랜덤한 4자리 인증번호 생성
            String numStr = generateRandomNumber();

            Message coolsms = new Message(apiKey, apiSecret); // 생성자를 통해 API 키와 API 시크릿 전달

            HashMap<String, String> params = new HashMap<>();
            params.put("to", to);    // 수신 전화번호
            params.put("from", fromPhoneNumber);    // 발신 전화번호
            params.put("type", "sms");
            params.put("text", "[SmartPay] 인증번호는 [" + numStr + "] 입니다.");

            // 메시지 전송
            //실제 문자 보내려면 여기만 주석해제하면됨
            //coolsms.send(params);

            // 생성된 인증번호를 Redis에 저장
            log.info("생성된 인증번호: " +numStr);
            System.out.println("생성된 인증번호: " +numStr);
            verificationService.saveVerificationCode(to, numStr);

            return numStr; // 생성된 인증번호 반환

        } catch (Exception e) {
            throw new CoolsmsException("Failed to send SMS: " + e.getMessage(), 0);
        }
    }

    // 랜덤한 4자리 숫자 생성 메서드
    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }
}
