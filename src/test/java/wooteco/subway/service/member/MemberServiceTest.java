package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.exceptions.InvalidLoginException;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private static final String TEST_USER_EMAIL = "brown@email.com";
    private static final String TEST_USER_NAME = "브라운";
    private static final String TEST_USER_PASSWORD = "brown";
    private static final String TEST_USER_NAME2 = "터틀";
    private static final String TEST_USER_PASSWORD2 = "turtle";
    private static final long TEST_USER_ID = 1L;

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider);
    }

    @DisplayName("회원 생성 테스트")
    @Test
    void createMember() {
        // given
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberRepository.save(any())).willReturn(member);

        // when
        memberService.createMember(member);

        // then
        verify(memberRepository).save(any());
    }

    @DisplayName("토큰 생성 테스트")
    @Test
    void createToken() {
        // given
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // when
        memberService.createToken(loginRequest);

        // then
        verify(jwtTokenProvider).createToken(anyString());
    }

    @DisplayName("토큰 생성 실패 테스트")
    @Test
    void createTokenException() {
        // given
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        String wrongPassword = "turtle";
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, wrongPassword);

        assertThatThrownBy(() -> memberService.createToken(loginRequest))
                .isInstanceOf(InvalidLoginException.class);
    }

    @DisplayName("회원 정보 조회 테스트")
    @Test
    void getMember() {
        // given
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        // when
        Member memberFound = memberService.findMemberByEmail(TEST_USER_EMAIL);

        // then
        assertThat(memberFound).isEqualTo(member);
    }

    @DisplayName("회원 정보 수정 테스트")
    @Test
    void updateMember() {
        // given
        Member member = new Member(TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        // when
        memberService.updateMember(member, new UpdateMemberRequest(TEST_USER_NAME2, TEST_USER_PASSWORD2));

        // then
        assertThat(member.getName()).isEqualTo(TEST_USER_NAME2);
        assertThat(member.getPassword()).isEqualTo(TEST_USER_PASSWORD2);
        verify(memberRepository).save(member);
    }

    @DisplayName("회원 정보 삭제 테스트")
    @Test
    void deleteMember() {
        // given
        Member member = new Member(TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        // when
        memberService.deleteMember(member);

        // then
        verify(memberRepository).deleteById(member.getId());
    }
}
