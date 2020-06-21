package woowa.bossdog.subway.service.Member;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import woowa.bossdog.subway.domain.Member;
import woowa.bossdog.subway.infra.JwtTokenProvider;
import woowa.bossdog.subway.repository.FavoriteRepository;
import woowa.bossdog.subway.repository.MemberRepository;
import woowa.bossdog.subway.service.Member.dto.LoginRequest;
import woowa.bossdog.subway.service.Member.dto.MemberRequest;
import woowa.bossdog.subway.service.Member.dto.MemberResponse;
import woowa.bossdog.subway.service.Member.dto.UpdateMemberRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MemberServiceTest {

    private MemberService memberService;

    @Mock private MemberRepository memberRepository;

    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        memberService = new MemberService(memberRepository, jwtTokenProvider);
    }

    @DisplayName("회원 생성")
    @Test
    void createMember() {
        // given
        final MemberRequest request = new MemberRequest("test@test.com", "bossdog", "test");
        final Member member = new Member(111L, request.getEmail(), request.getName(), request.getPassword());
        given(memberRepository.save(any())).willReturn(member);

        // when
        final MemberResponse response = memberService.createMember(request);

        // then
        verify(memberRepository).save(any());
        assertThat(response.getId()).isEqualTo(member.getId());
        assertThat(response.getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getName()).isEqualTo(member.getName());
    }

    @DisplayName("회원 목록 조회")
    @Test
    void listMembers() {
        // given
        final List<Member> members = Lists.newArrayList(
                new Member(111L, "test@test.com", "bossdog", "test"),
                new Member(155L, "one@one.com", "onedog", "test"));
        given(memberRepository.findAll()).willReturn(members);

        // when
        final List<MemberResponse> responses = memberService.listMembers();

        // then
        verify(memberRepository).findAll();
        assertThat(responses.get(0).getEmail()).isEqualTo("test@test.com");
        assertThat(responses.get(1).getEmail()).isEqualTo("one@one.com");
    }

    @DisplayName("회원 단건 조회")
    @Test
    void findMember() {
        // given
        Member member = new Member("test@test.com", "bossdog", "test");
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        // when
        MemberResponse response = memberService.findMember(member.getId());

        // then
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getName()).isEqualTo("bossdog");
        assertThat(response.getPassword()).isEqualTo("test");
    }

    @DisplayName("회원 정보 수정")
    @Test
    void updateMember() {
        // given
        final Member member = new Member(111L, "test@test.com", "bossdog", "test");
        final UpdateMemberRequest request = new UpdateMemberRequest("changedName", "changedPassword");

        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        // when
        memberService.updateMember(member.getId(), request);
        final MemberResponse findMember = memberService.findMember(member.getId());

        // then
        verify(memberRepository, times(2)).findById(eq(111L));
        assertThat(findMember.getName()).isEqualTo("changedName");
        assertThat(findMember.getPassword()).isEqualTo("changedPassword");
    }

    @DisplayName("회원 삭제")
    @Test
    void deleteMember() {
        // given
        final Member member = new Member(111L, "test@test.com", "bossdog", "test");

        // when
        memberService.deleteMember(member.getId());

        // then
        verify(memberRepository).deleteById(eq(111L));

    }

    @DisplayName("메일로 회원 조회")
    @Test
    void findMemberByEmail() {
        // given
        final Member member = new Member(111L, "test@test.com", "bossdog", "test");
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        // when
        final Member findMember = memberService.findMemberByEmail(member.getEmail());

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getPassword()).isEqualTo(member.getPassword());

    }

    @DisplayName("로그인 실패 - 비밀번호 틀릴 경우")
    @Test
    void loginFail() {
        final Member member = new Member(111L, "test@test.com", "bossdog", "test");
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        assertThatThrownBy(() -> {
            LoginRequest request = new LoginRequest("test@test.com", "wrong");
            memberService.createToken(request);
        }).isInstanceOf(WrongPasswordException.class);
    }

    @DisplayName("로그인 - 토큰 생성")
    @Test
    void createToken() {
        // given
        final Member member = new Member(111L, "test@test.com", "bossdog", "test");
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(jwtTokenProvider.createToken(any())).willReturn("ACCESS_TOKEN");

        // when
        LoginRequest request = new LoginRequest("test@test.com", "test");
        final String token = memberService.createToken(request);

        // then
        assertThat(token).isEqualTo("ACCESS_TOKEN");
    }

}