package org.shds.smartpay.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberTest {
    @Test
    public void testMemberCreationWithDefaultRegUser() {
        // 기본값인 "developer"를 사용하는 경우
        Member memberWithDefaultUser = Member.builder()
                .memberNo("M001")
                .email("user1@example.com")
                .pwd("password123")
                .name("John Doe")
                .phone("010-1234-5678")
                .fromSocial(false)
                .payPwd("paypwd123")
                .regUser("developer")
                .build();

        // 검증
        assertThat(memberWithDefaultUser.getMemberNo()).isEqualTo("M001");
        assertThat(memberWithDefaultUser.getEmail()).isEqualTo("user1@example.com");
        assertThat(memberWithDefaultUser.getRegUser()).isEqualTo("developer");
        assertThat(memberWithDefaultUser.getName()).isEqualTo("John Doe");
    }

    @Test
    public void testMemberCreationWithCustomRegUser() {
        // reg_user를 "customUser"로 설정하는 경우
        Member memberWithCustomUser = Member.builder()
                .memberNo("M002")
                .email("user2@example.com")
                .pwd("password456")
                .name("Jane Smith")
                .phone("010-9876-5432")
                .fromSocial(true)
                .payPwd("paypwd456")
                .regUser("customUser")
                .build();

        // 검증
        assertThat(memberWithCustomUser.getMemberNo()).isEqualTo("M002");
        assertThat(memberWithCustomUser.getEmail()).isEqualTo("user2@example.com");
        //assertThat(memberWithCustomUser.getRegUser()).isEqualTo("customUser");
        assertThat(memberWithCustomUser.getName()).isEqualTo("Jane Smith");
    }
}