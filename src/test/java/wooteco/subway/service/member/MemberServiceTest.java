package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

	public static final String UPDATED_TEST_USER_NAME = "류성현";
	public static final String UPDATED_TEST_USER_PASSWORD = "brown1234";

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider);
    }

	@DisplayName("회원을 등록하는 기능")
	@Test
	void createMember() {
		Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		memberService.createMember(member);

		verify(memberRepository).save(any());
	}

	@DisplayName("이메일로 회원을 조회하는 기능")
	@Test
	void findMemberByEmail() {
		Optional<Member> optionalMember = Optional.of(new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));
		when(memberRepository.findByEmail(anyString())).thenReturn(optionalMember);

		Member member = memberService.findMemberByEmail(TEST_USER_EMAIL);

		assertThat(member.getEmail()).isEqualTo(TEST_USER_EMAIL);
		assertThat(member.getName()).isEqualTo(TEST_USER_NAME);
		assertThat(member.getPassword()).isEqualTo(TEST_USER_PASSWORD);

	}

	@DisplayName("회원을 수정하는 기능")
	@Test
	void updateMemberByUser() {
		Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest(UPDATED_TEST_USER_NAME,
			UPDATED_TEST_USER_PASSWORD);

		Member savedMember = new Member(TEST_USER_EMAIL, UPDATED_TEST_USER_NAME, UPDATED_TEST_USER_PASSWORD);
		when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

		Member updatedMember = memberService.updateMemberByUser(member, updateMemberRequest);

		assertThat(updatedMember.getEmail()).isEqualTo(TEST_USER_EMAIL);
		assertThat(updatedMember.getName()).isEqualTo(UPDATED_TEST_USER_NAME);
		assertThat(updatedMember.getPassword()).isEqualTo(UPDATED_TEST_USER_PASSWORD);
	}

	@DisplayName("로그인 후 인증 토큰을 생성하는 기능")
	@Test
	void createToken() {
		Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
		LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		memberService.createToken(loginRequest);

		verify(jwtTokenProvider).createToken(anyString());
	}
}
