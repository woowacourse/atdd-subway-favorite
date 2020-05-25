package wooteco.subway.domain.member;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

@DataJdbcTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이름이 중복된 회원 추가시 DbActionExecutionException 발생")
    @Test
    void save() {
        String email = "sample@sample.com";
        String name = "sample";
        String password = "password";
        Member member = new Member(email, name, password);
        Member newMember = new Member(email, "hi", "hello");
        memberRepository.save(member);
        assertThrows(DbActionExecutionException.class, () -> memberRepository.save(newMember));
    }
}