package com.sparta.settlementsystem.member.repository;

import com.sparta.settlementsystem.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  boolean existsByEmail(String email);
}
