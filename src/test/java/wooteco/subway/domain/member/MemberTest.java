package wooteco.subway.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    public void construct() {
        Member member = new Member("test@test.com", "test", "tester");
        assertThat(member).isNotNull();
    }

    @Test
    public void update() {
        Member member = new Member("test@test.com", "test", "tester");
        member.update("new_test", "tester");
        assertThat(member.getName()).isEqualTo("new_test");

        member.update("new_test", "new_tester");
        assertThat(member.getPassword()).isEqualTo("new_tester");
    }

    @Test
    void checkPassword() {
        Member member = new Member("test@test.com", "test", "tester");
        assertThat(member.checkPassword("tester")).isTrue();
    }
}