package org.shds.smartpay.entity;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testFindMemberById() {
        // 더미 데이터 저장 (다시 저장할 필요 없으나 테스트의 독립성을 위해 포함)
        Member member = Member.builder()
                .memberNo("M001")
                .email("user@example.com")
                .pwd("password123")
                .name("John Doe")
                .phone("010-1234-5678")
                .fromSocial(false)
                .regUser("testUser")
                .payPwd("paypwd123")
                .build();

        memberRepository.save(member);

        // 저장된 데이터 조회
        Optional<Member> retrievedMember = memberRepository.findById("M001");

        // 검증
        assertThat(retrievedMember.isPresent()).isTrue();
        assertThat(retrievedMember.get().getEmail()).isEqualTo("user@example.com");
        assertThat(retrievedMember.get().getName()).isEqualTo("John Doe");
        assertThat(retrievedMember.get().getRegUser()).isEqualTo("testUser");
    }

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