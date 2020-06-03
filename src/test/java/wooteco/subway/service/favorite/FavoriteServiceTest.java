package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteExistenceResponse;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.station.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

	private static final long FIRST_STATION_ID = 1L;
	private static final long SECOND_STATION_ID = 2L;
	private static final long THIRD_STATION_ID = 3L;

	@Mock
	MemberRepository memberRepository;

	@Mock
	StationRepository stationRepository;

	private FavoriteService favoriteService;

	private Member member;

	@BeforeEach
	void setUp() {
		favoriteService = new FavoriteService(memberRepository, stationRepository);
		Favorites favorites = new Favorites(
			new HashSet<>(Arrays.asList(new Favorite(FIRST_STATION_ID,
				SECOND_STATION_ID), new Favorite(SECOND_STATION_ID, THIRD_STATION_ID))));
		member = new Member(1L, "sample@sample", "sample", "sample", favorites);
	}

	@DisplayName("회원의 즐겨찾기 노선 목록 전체를 조회")
	@Test
	void getFavorites() {
		Station gangnam = new Station(1L, "강남");
		Station gangbuk = new Station(2L, "강북");
		Station gangdong = new Station(3L, "강동");
		List<Station> stations = Arrays.asList(gangnam, gangbuk, gangdong);
		when(stationRepository.findAllById(anySet())).thenReturn(stations);
		List<FavoriteResponse> favorites1 = favoriteService.getFavorites(member);

		List<FavoriteResponse> expected = Arrays.asList(
			new FavoriteResponse(StationResponse.of(gangnam), StationResponse.of(gangbuk)),
			new FavoriteResponse(StationResponse.of(gangbuk), StationResponse.of(gangdong)));

		assertThat(favorites1).isEqualTo(expected);
	}

	@DisplayName("해당 경로가 즐겨찾기에 추가되어있는지 확인")
	@Test
	void hasFavoritePath() {
		FavoriteExistenceResponse expected = new FavoriteExistenceResponse(true);
		FavoriteExistenceResponse actual = favoriteService.hasFavoritePath(member, FIRST_STATION_ID,
			SECOND_STATION_ID);

		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("즐겨찾기 경로 추가")
	@Test
	void addFavorite() {
		FavoriteRequest request = new FavoriteRequest(FIRST_STATION_ID, THIRD_STATION_ID);
		favoriteService.addFavorite(member, request);
		verify(memberRepository).save(any());
		assertThat(member.getFavorites()).contains(request.toFavorite());
	}

	@DisplayName("즐겨찾기에서 경로 삭제")
	@Test
	void removeFavorite() {
		favoriteService.removeFavorite(member, FIRST_STATION_ID, SECOND_STATION_ID);

		verify(memberRepository).save(any());
		assertThat(
			member.getFavorites().contains(new Favorite(FIRST_STATION_ID, SECOND_STATION_ID)))
			.isFalse();

	}
}