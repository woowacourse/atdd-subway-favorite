package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static wooteco.subway.AcceptanceTest.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteDeleteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
	FavoriteService favoriteService;

	@Mock
	MemberRepository memberRepository;

	@Mock
	StationRepository stationRepository;

	@BeforeEach
	void setUp() {
		favoriteService = new FavoriteService(memberRepository, stationRepository);
	}

	@DisplayName("즐겨찾기 추가")
	@Test
	void create() {
		Member member = Member.of(TEST_USER_EMAIL, TEST_USER_EMAIL, TEST_USER_PASSWORD);

		when(memberRepository.findById(any())).thenReturn(Optional.of(member));
		when(stationRepository.findIdByName("강남역")).thenReturn(Optional.of(1L));
		when(stationRepository.findIdByName("잠실역")).thenReturn(Optional.of(2L));

		FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest("강남역",
			"잠실역");
		favoriteService.create(member, favoriteCreateRequest);

		verify(memberRepository).save(member);
	}

	@DisplayName("즐겨찾기 조회")
	@Test
	void find() {
		Member member = Member.of(TEST_USER_EMAIL, TEST_USER_EMAIL, TEST_USER_PASSWORD);
		member.addFavorite(Favorite.of(1L, 2L));

		List<FavoriteResponse> expect = Lists.newArrayList(
			new FavoriteResponse("강남역", "잠실역"));

		when(memberRepository.findById(any())).thenReturn(Optional.of(member));
		when(stationRepository.findAllById(any())).thenReturn(
			Arrays.asList(new Station(1L, "강남역"), new Station(2L, "잠실역")));

		List<FavoriteResponse> favoriteResponses = favoriteService.find(member);

		assertThat(favoriteResponses).isEqualTo(expect);
	}

	@DisplayName("즐겨찾기 삭제")
	@Test
	void delete() {
		Member member = Member.of(TEST_USER_EMAIL, TEST_USER_EMAIL, TEST_USER_PASSWORD);
		member.addFavorite(Favorite.of(1L, 2L));

		when(memberRepository.findById(any())).thenReturn(Optional.of(member));
		when(stationRepository.findIdByName("강남역")).thenReturn(Optional.of(1L));
		when(stationRepository.findIdByName("잠실역")).thenReturn(Optional.of(2L));

		FavoriteDeleteRequest favoriteDeleteRequest = new FavoriteDeleteRequest("강남역",
			"잠실역");
		favoriteService.delete(member, favoriteDeleteRequest);

		verify(memberRepository).save(any());
	}
}