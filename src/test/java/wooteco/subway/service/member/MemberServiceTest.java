package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";
    private static final Long TEST_USER_ID = 1L;

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private Member member;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider);
        member = new Member(TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    void createMember() {
        memberService.createMember(member);

        verify(memberRepository).save(any());
    }

    @Test
    void createToken() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @Test
    void findMemberByEmail() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        Member result = memberService.findMemberByEmail(TEST_USER_EMAIL);

        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
    }

    @Test
    void updateMember() {
        String updatedName = "updatedName";
        String updatedPassword = "updatedPassword";

        when(memberRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(any());

        memberService.updateMember(TEST_USER_ID, new UpdateMemberRequest(updatedName, updatedPassword));

        assertThat(member.getName()).isEqualTo(updatedName);
        assertThat(member.getPassword()).isEqualTo(updatedPassword);
    }

    @Test
    void deleteMember() {
        doNothing().when(memberRepository).deleteById(TEST_USER_ID);
        memberService.deleteMember(TEST_USER_ID);
    }
}
