package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
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

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
	public static final String TEST_USER_EMAIL = "brown@email.com";
	public static final String TEST_USER_NAME = "브라운";
	public static final String TEST_USER_PASSWORD = "brown";

	private FavoriteService favoriteService;
	private Member member;

	@Mock
	private FavoriteRepository favoriteRepository;
	@Mock
	private StationRepository stationRepository;

	@BeforeEach
	void setUp() {
		this.favoriteService = new FavoriteService(favoriteRepository, stationRepository);
		member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
	}

	@DisplayName("즐겨찾기에 경로 추가한다")
	@Test
	void saveFavorite() {
		Favorite value = new Favorite(1L, 1L, 2L, 3L);
		given(favoriteRepository.save(any())).willReturn(value);
		given(favoriteRepository.findByMemberId(1L)).willReturn(Arrays.asList(value));

		FavoriteRequest request = new FavoriteRequest(1L, 2L);
		Long id = favoriteService.add(member, request);

		assertThat(id).isNotNull();
	}

	@DisplayName("즐겨찾기에 있는 경로 삭제한다")
	@Test
	void deleteFavorite() {
		given(favoriteRepository.findById(anyLong()))
			.willReturn(Optional.of(new Favorite(1L, 2L, 3L)));

		favoriteService.delete(member, 1L);

		verify(favoriteRepository).delete(any());
	}

	@DisplayName("즐겨찾기에 있는 경로 조회한다")
	@Test
	void findAll() {
		given(favoriteRepository.findByMemberId(anyLong())).willReturn(Arrays.asList(new Favorite(1L, 1L, 2L),
			new Favorite(1L, 2L, 3L), new Favorite(1L, 3L, 4L)));
		given(stationRepository.findAll()).willReturn(Arrays.asList(new Station(1L, "일원역"),
			new Station(2L, "이대역"), new Station(3L, "삼성역"), new Station(4L, "사당역")));

		List<FavoriteResponse> allFavoritesByMemberId = favoriteService.findFavorites(member);

		assertThat(allFavoritesByMemberId).isNotNull();
		assertThat(allFavoritesByMemberId).hasSize(3);
	}
}
