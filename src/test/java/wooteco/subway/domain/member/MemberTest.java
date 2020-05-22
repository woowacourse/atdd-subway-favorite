package wooteco.subway.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {
    @Test
    void isNotEqualFailTest() {
        Member member = new Member("ramen@gmail.com", "a", "a");
        assertThat(member.isNotEqualEmail("ramen@gmail.com")).isFalse();
    }

    @Test
    void isNotEqualSuccessTest() {
        Member member = new Member("ramen@gmail.com", "a", "a");
        assertThat(member.isNotEqualEmail("ra@gmail.com")).isTrue();
    }
}