package com.example.springedatajpainflearn.member;

public class MemberDto {
    private Long id;
    private String username;
    private int age;
    private String teamName;

    public MemberDto(Long id, String username, int age, String teamName) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.teamName = teamName;
    }
}
