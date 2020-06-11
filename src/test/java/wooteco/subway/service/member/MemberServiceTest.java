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

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.exception.NotFoundMemberException;
import wooteco.subway.web.member.exception.NotMatchPasswordException;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_OTHER_USER_EMAIL = "pobi@email.com";
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_OTHER_USER_NAME = "포비";
    public static final String TEST_USER_PASSWORD = "brown";
    public static final String TEST_OTHER_USER_PASSWORD = "pobi";
    public static final long TEST_USER_ID = 1L;

    private MemberService memberService;
    private Member member;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider);
        member = new Member(TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    void createMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        memberService.save(member);

        verify(memberRepository).save(member);
    }

    @Test
    void findByEmail() {
        when(memberRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(member));

        Member expect = memberService.findMemberByEmail(TEST_USER_EMAIL);
        assertThat(expect).isEqualToComparingFieldByField(member);
    }

    @Test
    void findByNotExistEmail() {
        when(memberRepository.findByEmail(TEST_OTHER_USER_EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findMemberByEmail(TEST_OTHER_USER_EMAIL))
            .isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(loginRequest.getEmail());
    }

    @Test
    void createTokenWrongPassword() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_OTHER_USER_PASSWORD);

        assertThatThrownBy(() -> memberService.createToken(loginRequest)).
            isInstanceOf(NotMatchPasswordException.class);
    }

    @Test
    void updateMember() {
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(memberRepository.save(any())).thenReturn(member);

        memberService.updateMember(member.getId(), new UpdateMemberRequest(
            "NEW_" + TEST_USER_NAME, TEST_USER_PASSWORD, "NEW_" + TEST_USER_PASSWORD));
        assertThat(member.getName()).isEqualTo("NEW_" + TEST_USER_NAME);
        assertThat(member.getPassword()).isEqualTo("NEW_" + TEST_USER_PASSWORD);
    }

    @Test
    void updateMemberWrongPassword() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest(
            "NEW_" + TEST_USER_NAME, TEST_OTHER_USER_PASSWORD, "NEW_" + TEST_USER_PASSWORD);

        assertThatThrownBy(() -> memberService.updateMember(member.getId(), updateMemberRequest))
            .isInstanceOf(NotMatchPasswordException.class);
    }

    @Test
    void deleteMember() {
        memberService.deleteMember(member.getId());
        verify(memberRepository).deleteById(member.getId());
    }
}
