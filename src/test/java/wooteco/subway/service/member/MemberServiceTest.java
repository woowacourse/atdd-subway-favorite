package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;

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
        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME,
            TEST_USER_PASSWORD);
        when(memberRepository.save(any(Member.class))).thenReturn(memberRequest.toMember());

        memberService.createMember(memberRequest);

        verify(memberRepository).save(any());
    }

    @Test
    void duplicateEmail() {
        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME,
            TEST_USER_PASSWORD);
        DbActionExecutionException dbActionExecutionException =
            new DbActionExecutionException(null, new DuplicateKeyException("중복되는 키 값"));
        when(memberRepository.save(any())).thenThrow(dbActionExecutionException);

        assertThatThrownBy(() -> memberService.createMember(memberRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }
}
