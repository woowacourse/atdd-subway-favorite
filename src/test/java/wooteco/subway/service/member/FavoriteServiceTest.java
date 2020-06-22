package wooteco.subway.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.station.DuplicateFavoriteException;
import wooteco.subway.service.station.NotFoundStationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * class description
 *
 * @author HyungJu An, Minwoo Yim
 */
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
	public static final String TEST_USER_EMAIL = "brown@email.com";
	public static final String TEST_USER_NAME = "브라운";
	public static final String TEST_USER_PASSWORD = "brown";

	private FavoriteService favoriteService;

	@Mock
	private StationRepository stationRepository;

	@BeforeEach
	void setUp() {
		this.favoriteService = new FavoriteService(stationRepository);
	}

	@Test
	void addFavorite() {
		Member member = Member.of(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);

		Station station1 = Station.of(1L, "잠실역");
		Station station2 = Station.of(2L, "강남역");
		when(stationRepository.findById(1L)).thenReturn(Optional.of(station1));
		when(stationRepository.findById(2L)).thenReturn(Optional.of(station2));

		favoriteService.createFavorite(member, favoriteRequest);

		assertThat(member.getFavorites()).isNotEmpty();
	}

	@DisplayName("출발역이 없는 경우 예외처리한다.")
	@Test
	void addFavorite2() {
		Member member = Member.of(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);

		when(stationRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> favoriteService.createFavorite(member, favoriteRequest))
				.isInstanceOf(NotFoundStationException.class)
				.hasMessageContaining("출발역");
	}

	@DisplayName("도착역이 없는 경우 예외처리한다.")
	@Test
	void addFavorite3() {
		Member member = Member.of(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);

		when(stationRepository.findById(1L)).thenReturn(Optional.of(Station.of("잠실역")));
		when(stationRepository.findById(2L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> favoriteService.createFavorite(member, favoriteRequest))
				.isInstanceOf(NotFoundStationException.class)
				.hasMessageContaining("도착역");
	}

	@DisplayName("이미 있는 즐겨찾기인 경우 예외처리한다.")
	@Test
	void addFavorite4() {
		final Member member = Member.of(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
		Station station1 = Station.of(1L, "잠실역");
		Station station2 = Station.of(2L, "강남역");
		Favorite favorite = Favorite.of(1L, station1, station2);

		when(stationRepository.findById(1L)).thenReturn(Optional.of(station1));
		when(stationRepository.findById(2L)).thenReturn(Optional.of(station2));

		member.addFavorite(favorite);

		assertThatThrownBy(() -> favoriteService.createFavorite(member, favoriteRequest))
				.isInstanceOf(DuplicateFavoriteException.class)
				.hasMessageContaining("존재하는");
	}

	@Test
	void getFavorites() {
		final Member member = Member.of(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Station station1 = Station.of(1L, "잠실역");
		Station station2 = Station.of(2L, "강남역");
		Favorite favorite = Favorite.of(1L, station1, station2);
		member.addFavorite(favorite);

		assertThat(favoriteService.getFavorites(member)).hasSize(1);
	}

	@Test
	void deleteFavorite() {
		final Member member = Member.of(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Station station1 = Station.of(1L, "잠실역");
		Station station2 = Station.of(2L, "강남역");
		Favorite favorite = Favorite.of(1L, station1, station2);
		member.addFavorite(favorite);

		favoriteService.deleteFavorite(member, 1L);

		assertThat(member.getFavorites()).isEmpty();
	}
}
