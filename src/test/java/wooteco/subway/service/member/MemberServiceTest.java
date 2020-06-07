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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    void createMember() {
        memberService.createMember(member);
        verify(memberRepository).save(any());
    }

    @Test
    void findByEmail() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(member));
        Member foundMember = memberService.findMemberByEmail(member.getEmail());
        assertThat(member).isEqualToComparingFieldByField(foundMember);
    }

    @Test
    void update() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(member));
        when(memberRepository.save(member)).thenReturn(member);

        memberService.updateMember(1L, new UpdateMemberRequest("오렌지", TEST_USER_PASSWORD, "똑똑"));
        assertThat(member).isEqualToComparingFieldByField(member);
    }

    @Test
    void delete() {
        doNothing().when(memberRepository).deleteById(1L);
        when(memberRepository.findById(1L)).thenThrow(IllegalArgumentException.class);
        memberService.deleteMember(1L);

        assertThatThrownBy(() -> {
            memberService.findById(1L);
        }).isInstanceOf(IllegalArgumentException.class);
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
