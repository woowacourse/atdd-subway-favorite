package wooteco.subway.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_EMAIL;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_NAME;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_PASSWORD;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MemberTest {
    @CsvSource(value = {"1q2w3e,false", TEST_USER_PASSWORD + ",true"})
    @ParameterizedTest
    void checkPassword(String password, boolean expect) {
        Member MEMBER_BROWN = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        assertThat(MEMBER_BROWN.checkPassword(password)).isEqualTo(expect);
    }
}
