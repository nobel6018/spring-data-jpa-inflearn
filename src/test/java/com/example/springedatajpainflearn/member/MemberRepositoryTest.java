package com.example.springedatajpainflearn.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("@Query에 파라미터 바인딩")
    public void testQueryAnnotation() {
        // given
        Member member1 = memberRepository.save(new Member("member1", 10));
        Member member2 = memberRepository.save(new Member("member2", 15));
        Member member3 = memberRepository.save(new Member("member3", 20));

        // when
        Member member = memberRepository.findUser("member2", 15);

        // then
        assertThat(member).isEqualTo(member2);
    }

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

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 15));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 25));
        memberRepository.save(new Member("member5", 30));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);

        // 같은 transaction이면 같은 entity manager를 사용한다
        // jpql을 적으면 영속성 context 내보내고 jpql 실행한다 -> 그래서 em.flush() 필요가 없다
        // 벌크 연산 이후에는 영속성 context 날려야 한다
        em.clear();

        Optional<Member> member1 = memberRepository.findByUsername("member5");
        Member member = member1.get();

        // then
        assertThat(resultCount).isEqualTo(3);
        assertThat(member.getAge()).isEqualTo(31); // em.clear() 했기 때문에. 영속성 컨텍스트 초기화됐고 query 다시 나가서 31로 업데이트 됨
    }
}