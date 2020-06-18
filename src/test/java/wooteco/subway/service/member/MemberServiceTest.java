package wooteco.subway.service.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	public static final String TEST_USER_EMAIL = "brown@email.com";
	public static final String TEST_USER_NAME = "브라운";
	public static final String TEST_USER_PASSWORD = "brown";

	private MemberService memberService;
	private Member member;

	@Mock
	private StationRepository stationRepository;
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
	void updateMember() {
		given(memberRepository.findById(any())).willReturn(Optional.of(member));
		UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("CU", "1234");
		memberService.updateMember(1L, updateMemberRequest);
		verify(memberRepository).save(any());
	}

	@Test
	void deleteMember() {
		doNothing().when(memberRepository).deleteById(any());
		memberService.deleteMember(1L);
		verify(memberRepository).deleteById(any());
	}
}
