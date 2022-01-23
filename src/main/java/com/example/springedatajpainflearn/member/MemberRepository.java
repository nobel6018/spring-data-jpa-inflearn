package com.example.springedatajpainflearn.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select m from Member m where m.age = :age", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
}
