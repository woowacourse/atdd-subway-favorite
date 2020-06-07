package wooteco.subway.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void update() {
        Member member = new Member("origin@naver.com", "name", "password");
        String updatedName = "updatedName";
        String updatedPassword = "updatedPassword";

        member.update(updatedName, updatedPassword);

        assertThat(member.getName()).isEqualTo(updatedName);
        assertThat(member.getPassword()).isEqualTo(updatedPassword);
    }
}