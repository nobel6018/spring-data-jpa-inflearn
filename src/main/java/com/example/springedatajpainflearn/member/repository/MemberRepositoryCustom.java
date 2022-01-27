package com.example.springedatajpainflearn.member.repository;

import com.example.springedatajpainflearn.member.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
