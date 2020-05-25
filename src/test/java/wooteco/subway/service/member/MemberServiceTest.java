package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static wooteco.subway.web.member.interceptor.BearerAuthInterceptor.*;

import java.util.Optional;

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
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.exception.MemberCreationException;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    private MemberService memberService;
    private Member member;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider);
        member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    void createMember() {
        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME,
            TEST_USER_PASSWORD);
        Member member = memberRequest.toMember();
        when(memberRepository.save(any())).thenReturn(member);

        MemberResponse response = memberService.createMember(memberRequest);

        verify(memberRepository).save(any());
        assertThat(response.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void createToken() {
        String mockToken = BEARER + "secret";
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(jwtTokenProvider.createToken(TEST_USER_EMAIL)).thenReturn(mockToken);

        String token = memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
        assertThat(token).isEqualTo(mockToken);
    }

    @Test
    void findMemberByEmail() {
        when(memberRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(member));

        Member actualMember = memberService.findMemberByEmail(TEST_USER_EMAIL);

        verify(memberRepository).findByEmail(TEST_USER_EMAIL);
        assertThat(actualMember).isEqualTo(member);
    }

    @Test
    void updateMember() {
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("NEW_RYAN",
            "NEW_RYAN_PASSWORD");
        Member updatedMember = new Member(member.getId(), member.getEmail(),
            updateMemberRequest.getName(), updateMemberRequest.getPassword());

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(memberRepository.save(any())).thenReturn(updatedMember);

        memberService.updateMember(member.getId(), updateMemberRequest);

        assertThat(member.getName()).isEqualTo(updatedMember.getName());
        assertThat(member.getPassword()).isEqualTo(updatedMember.getPassword());
    }

    @Test
    void deleteMember() {
        memberService.deleteMember(member.getId());
        verify(memberRepository).deleteById(member.getId());
    }

    @DisplayName("(예외) 회원가입 시 빈 문자열 입력")
    @Test
    void failedCreateMemberByBlank() {
        MemberRequest memberRequest = new MemberRequest("", "", "");
        when(memberRepository.save(any())).thenThrow(MemberCreationException.class);

        assertThatThrownBy(() -> memberService.createMember(memberRequest))
            .isInstanceOf(MemberCreationException.class);
    }
}
