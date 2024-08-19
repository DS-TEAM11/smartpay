package org.shds.smartpay.repository;

import org.shds.smartpay.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByName(String name);
}
