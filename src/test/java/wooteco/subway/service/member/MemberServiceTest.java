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
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
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
		Member member = Member.of(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		memberService.createMember(member);

		verify(memberRepository).save(any());
	}

	@DisplayName("email이 이미 존재하는 경우 예외 처리를 한다.")
	@Test
	void createMember2() {
		Member member = Member.of(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));

		assertThatThrownBy(() -> memberService.createMember(member))
				.isInstanceOf(DuplicateEmailException.class)
				.hasMessageContaining("가입된");
	}

	@DisplayName("업데이트 시 잘못된 id가 입력되면 예외 처리를 한다.")
	@Test
	void updateMember() {
		when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> memberService.updateMember(1L, new UpdateMemberRequest()))
				.isInstanceOf(NotFoundMemberException.class)
				.hasMessageContaining("해당하는");
	}

	@Test
	void createToken() {
		Member member = Member.of(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
		LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		memberService.createToken(loginRequest);

		verify(jwtTokenProvider).createToken(anyString());
	}

	@DisplayName("로그인 이메일이 없는 경우 예외 처리를 한다.")
	@Test
	void createToken2() {
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		assertThatThrownBy(() -> memberService.createToken(loginRequest))
				.isInstanceOf(NotFoundMemberException.class)
				.hasMessageContaining("해당하는");
	}

	@DisplayName("비밀번호가 틀렸을 경우 예외 처리를 한다.")
	@Test
	void createToken3() {
		Member member = Member.of(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
		LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, "abcd");

		assertThatThrownBy(() -> memberService.createToken(loginRequest))
				.isInstanceOf(IllegalPasswordException.class)
				.hasMessageContaining("잘못된");
	}

	@DisplayName("찾는 이메일이 없을 경우 예외 처리를한다.")
	@Test
	void findMemberByEmail() {
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		assertThatThrownBy(() -> memberService.findMemberByEmail("a@email.com"))
				.isInstanceOf(NotFoundMemberException.class)
				.hasMessageContaining("해당하는");
	}
}
