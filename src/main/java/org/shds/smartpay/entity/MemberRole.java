package org.shds.smartpay.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    USER("ROLE_USER"), SELLER("ROLE_SELLER"), ADMIN("ROLE_ADMIN"),GUEST("ROLE_GUEST");
    private final String key;
}
