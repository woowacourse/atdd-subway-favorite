package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.web.member.InvalidRegisterException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider);
    }

    @Test
    void createMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(member);

        verify(memberRepository).save(any());
    }

    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @DisplayName("이메일 형식이 아닌 경우 예외 처리")
    @Test
    void createTokenNotEmailFormat() {
        Member member = new Member("abc", "abc", "abc");

        assertThatThrownBy(() -> memberService.createMember(member))
                .isInstanceOf(InvalidRegisterException.class)
                .hasMessage(InvalidRegisterException.INVALID_EMAIL_FORMAT_MSG);
    }

    @DisplayName("이메일이 중복되는 경우 예외 처리")
    @Test
    void createTokenEmailDuplicate() {
        Member member = new Member("abc@abc.com", "abc", "abc");
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.createMember(member))
                .isInstanceOf(InvalidRegisterException.class)
                .hasMessage(InvalidRegisterException.DUPLICATE_EMAIL_MSG);
    }
}
