package com.example.springedatajpainflearn.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testPage() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        // when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(10, pageRequest);

        // .map 함수를 사용하여 page 내부의 데이터 변경
        // .map 함수는 Page 인터페이스에 구현
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), m.getAge(), null));

        // then
        List<Member> content = page.getContent();


        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수. 전체 카운트 쿼리
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호. !! 페이지는 0부터 시작
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 번호
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        assertThat(page.hasNext()).isTrue();
    }
}