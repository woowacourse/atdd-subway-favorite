package wooteco.subway.service.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.web.member.exception.DuplicateException;
import wooteco.subway.web.member.exception.NotExistMemberDataException;

@SpringBootTest
@Sql("/truncate.sql")
public class MemberServiceIntegrationTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_PASSWORD = "brown";
    private static final String TEST_USER_NAME = "Brown";

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private MemberService memberService;


    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider);
        memberRepository.save(new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));
    }

    @DisplayName("로그인 email 정보가 없을 경우 익셉션이 발생한다")
    @Test
    void createTokenFail() {
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        Assertions.assertThatThrownBy(() -> memberService.createToken(loginRequest))
                .isInstanceOf(NotExistMemberDataException.class);
    }

    @DisplayName("이미 저장된 member일경우 Exception이 발생한다 ")
    @Test
    void createMemberFailTest() {

        Assertions.assertThatThrownBy(() -> memberService.createMember(memberRepository.save(new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD))))
        .isInstanceOf(DuplicateException.class);
    }

}
