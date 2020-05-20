package wooteco.subway;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import wooteco.subway.web.member.NotMatchPasswordException;

public class MemberAcceptanceTest extends AcceptanceTest {

    // 회원 정보를 관리한다.
    // 회원 가입을 한다.

    @Test
    void memberAcceptanceTest() {
        assertThat(registerMember("dd@naver.com", "디디", "1q2w3e4r", "1q2w3e4r")).isEqualTo("/members/1");
        assertThat(registerMember("fucct@naver.com", "둔덩", "qwerqwer", "qwerqwer")).isEqualTo("/members/2");

    }
}
