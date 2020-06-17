package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;

@Sql("/truncate.sql")
@TestPropertySource(locations = "classpath:application-test.properties")
@Import({MemberService.class, JwtTokenProvider.class})
@DataJdbcTest
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원가입")
    @Test
    void createMember() {
        Member expect = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        Member actual = memberService.createMember(expect);

        assertThat(actual.getId()).isEqualTo(expect.getId());
    }

    @DisplayName("회원가입 후 토큰 발급")
    @Test
    void createToken() {
        Member member = memberRepository.save(new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));
        LoginRequest loginRequest = new LoginRequest(member.getEmail(), member.getPassword());

        String token = memberService.createToken(loginRequest);

        assertThat(token).isNotBlank();
    }
}
