package org.shds.smartpay.security.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.entity.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

@Log4j2
@Getter
@Setter
@ToString
public class MemberRegisterDTO extends User {
    private String email;

    private String password;

    private String phone;

    private String name;

    public MemberRegisterDTO(String email,String password,String phone,String name,
                             Collection<? extends GrantedAuthority> authorities) {
        super(email,password,authorities != null ? authorities : Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.name=name;

    }

}
