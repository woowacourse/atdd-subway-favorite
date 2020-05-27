package wooteco.subway.service.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.web.member.exception.NotExistMemberDataException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MemberServiceIntegrationTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_PASSWORD = "brown";

    private MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider);
    }

    @DisplayName("로그인 email 정보가 없을 경우 익셉션이 발생한다")
    @Test
    void createTokenFail() {
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        Assertions.assertThatThrownBy(() -> memberService.createToken(loginRequest))
                .isInstanceOf(NotExistMemberDataException.class);
    }

}
