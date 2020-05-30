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
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.member.dto.LoginRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	public static final String TEST_USER_EMAIL = "brown@email.com";
	public static final String TEST_USER_NAME = "브라운";
	public static final String TEST_USER_PASSWORD = "brown";

	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private FavoriteService favoriteService;
	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		this.memberService = new MemberService(memberRepository, favoriteService, jwtTokenProvider);
	}

	@DisplayName("회원가입을 성공하는 테스트")
	@Test
	void createMember() {
		Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		memberService.createMember(member);

		verify(memberRepository).save(any());
	}

	@DisplayName("로그인이 성공하는 테스트")
	@Test
	void createToken() {
		Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
		LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		memberService.createToken(loginRequest);

		verify(jwtTokenProvider).createToken(anyString());
	}

	@DisplayName("존재하지 않는 이메일로 로그인이 실패하는 테스트")
	@Test
	void notExistEmailNotCreateToken() {
		LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL + "NOT_EXIST", TEST_USER_PASSWORD);

		assertThatThrownBy(() -> memberService.createToken(loginRequest)).isInstanceOf(NonExistEmailException.class)
			.hasMessageContaining("존재하지않는 이메일 입니다.");
	}

	@DisplayName("잘못된 패스워드로 로그인이 실패하는 테스트")
	@Test
	void wrongPasswordNotCreateToken() {
		Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

		LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD + "WRONG_PASSWORD");

		assertThatThrownBy(() -> memberService.createToken(loginRequest)).isInstanceOf(WrongPasswordException.class)
			.hasMessage("잘못된 패스워드입니다.");
	}
}
