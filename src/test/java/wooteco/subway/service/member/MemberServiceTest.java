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
		// member.addFavorite(new Favorite(1L, 2L));
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

	// @DisplayName("즐겨찾기에 경로 추가")
	// @Test
	// void saveFavorite() {
	// 	FavoriteRequest favorite = new FavoriteRequest(1L, 2L);
	//
	// 	Station 일원역 = new Station(1L, "일원역");
	// 	Station 잠실역 = new Station(2L, "잠실역");
	//
	// 	given(memberRepository.save(any())).willReturn(any());
	// 	given(stationRepository.findById(1L)).willReturn(Optional.of(일원역));
	// 	given(stationRepository.findById(2L)).willReturn(Optional.of(잠실역));
	//
	// 	FavoriteResponse favoriteResponse = memberService.saveFavorite(member, favorite);
	//
	// 	assertThat(favoriteResponse.getSourceStation()).isEqualTo(일원역.getName());
	// 	assertThat(favoriteResponse.getTargetStation()).isEqualTo(잠실역.getName());
	// }@DisplayName("즐겨찾기에 경로 추가")
	// @Test
	// void saveFavorite() {
	// 	FavoriteRequest favorite = new FavoriteRequest(1L, 2L);
	//
	// 	Station 일원역 = new Station(1L, "일원역");
	// 	Station 잠실역 = new Station(2L, "잠실역");
	//
	// 	given(memberRepository.save(any())).willReturn(any());
	// 	given(stationRepository.findById(1L)).willReturn(Optional.of(일원역));
	// 	given(stationRepository.findById(2L)).willReturn(Optional.of(잠실역));
	//
	// 	FavoriteResponse favoriteResponse = memberService.saveFavorite(member, favorite);
	//
	// 	assertThat(favoriteResponse.getSourceStation()).isEqualTo(일원역.getName());
	// 	assertThat(favoriteResponse.getTargetStation()).isEqualTo(잠실역.getName());
	// }

	// @DisplayName("즐겨찾기에 있는 경로 삭제")
	// @Test
	// void deleteFavorite() {
	// 	given(memberRepository.save(any())).willReturn(any());
	//
	// 	Favorite favorite = new Favorite(1L, 2L);
	// 	memberService.deleteFavorite(member, FavoriteRequest.of(favorite));
	//
	// 	verify(memberRepository).save(any());
	// }
	//
	// @DisplayName("즐겨찾기에 있는 경로 조회")
	// @Test
	// void findAll() {
	// 	member.addFavorite(new Favorite(1L, 2L));
	// 	member.addFavorite(new Favorite(2L, 3L));
	// 	member.addFavorite(new Favorite(3L, 4L));
	//
	// 	given(stationRepository.findAll()).willReturn(Arrays.asList(new Station(1L, "일원역"),
	// 		new Station(2L, "이대역"), new Station(3L, "삼성역"), new Station(4L, "사당역")));
	//
	// 	List<FavoriteResponse> allFavoritesByMemberId = memberService.findAllFavoritesByMember(
	// 		member);
	//
	// 	assertThat(allFavoritesByMemberId).isNotNull();
	// 	assertThat(allFavoritesByMemberId).hasSize(3);
	// }
}
