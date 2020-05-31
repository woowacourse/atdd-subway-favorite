package wooteco.subway.service.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exceptions.DuplicatedFavoritePathException;
import wooteco.subway.exceptions.NotExistFavoritePathException;
import wooteco.subway.service.favorite.dto.FavoritePathResponse;
import wooteco.subway.service.station.StationService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
	private FavoriteService favoriteService;

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private StationService stationService;

	@BeforeEach
	void setUp() {
		this.favoriteService = new FavoriteService(memberRepository, stationService);
	}

	@DisplayName("즐겨찾기 경로 등록에 성공하는지 확인")
	@Test
	void registerPath() {
		Member expectedMember = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Station source = new Station(1L, STATION_NAME_KANGNAM);
		Station target = new Station(2L, STATION_NAME_HANTI);
		FavoritePath favoritePathToAdd = new FavoritePath(1L, 1L, 2L);
		expectedMember.addFavoritePath(favoritePathToAdd);

		BDDMockito.when(stationService.findStationByName(STATION_NAME_KANGNAM)).thenReturn(source);
		BDDMockito.when(stationService.findStationByName(STATION_NAME_HANTI)).thenReturn(target);
		BDDMockito.when(memberRepository.save(any())).thenReturn(expectedMember);

		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		FavoritePath favoritePath = favoriteService.registerPath(member, STATION_NAME_KANGNAM, STATION_NAME_HANTI);

		assertThat(favoritePath.getId()).isEqualTo(1L);
		assertThat(favoritePath.getSourceId()).isEqualTo(1L);
		assertThat(favoritePath.getTargetId()).isEqualTo(2L);
	}

	@DisplayName("이미 등록된 즐겨찾기 경로를 재등록 시 실패하는지 확인")
	@Test
	void registerPathFailedWhenDuplicatedRegister() {
		Member expectedMember = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Station source = new Station(1L, STATION_NAME_KANGNAM);
		Station target = new Station(2L, STATION_NAME_HANTI);
		FavoritePath favoritePathToAdd = new FavoritePath(1L, 1L, 2L);
		expectedMember.addFavoritePath(favoritePathToAdd);

		BDDMockito.when(stationService.findStationByName(STATION_NAME_KANGNAM)).thenReturn(source);
		BDDMockito.when(stationService.findStationByName(STATION_NAME_HANTI)).thenReturn(target);

		assertThatThrownBy(() -> favoriteService.registerPath(expectedMember, source.getName(), target.getName()))
				.isInstanceOf(DuplicatedFavoritePathException.class)
				.hasMessage("이미 등록된 즐겨찾기 경로입니다!");
	}


	@DisplayName("즐겨찾기 경로 조회에 성공하는지 확인")
	@Test
	void retrievePath() {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Station kangnam = new Station(1L, STATION_NAME_KANGNAM);
		Station hanti = new Station(2L, STATION_NAME_HANTI);
		Station dogok = new Station(3L, STATION_NAME_DOGOK);
		Station yangjae = new Station(4L, STATION_NAME_YANGJAE);
		FavoritePath favoritePath1 = new FavoritePath(1L, 1L, 2L);
		FavoritePath favoritePath2 = new FavoritePath(2L, 3L, 4L);
		member.addFavoritePath(favoritePath1);
		member.addFavoritePath(favoritePath2);

		BDDMockito.when(stationService.findStationsByIds(anyList())).thenReturn(Arrays.asList(kangnam, hanti, dogok,
		                                                                                      yangjae));

		List<FavoritePathResponse> favoritePathRespons = favoriteService.retrievePath(member);

		assertThat(favoritePathRespons).hasSize(2);
		assertThat(favoritePathRespons.get(0).getId()).isNotNull();
		assertThat(favoritePathRespons.get(0).getSource().getName()).isEqualTo(STATION_NAME_KANGNAM);
	}

	@DisplayName("즐겨찾기 경로 삭제에 성공하는지 확인")
	@Test
	void deletePath() {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		FavoritePath favoritePath1 = new FavoritePath(1L, 1L, 2L);
		FavoritePath favoritePath2 = new FavoritePath(2L, 3L, 4L);
		member.addFavoritePath(favoritePath1);
		member.addFavoritePath(favoritePath2);

		favoriteService.deletePath(member, 1L);

		assertThat(member.getFavoritePathsIds()).hasSize(1);
		assertThat(member.getFavoritePathsStationsIds().get(0)).isEqualTo(3L);
	}

	@DisplayName("다른 회원의 즐겨찾기 경로 삭제 시도 시 실패하는지 확인")
	@Test
	void deletePathFailedWhenDeleteNotMyPath() {
		Member other = new Member(2L, "other " + TEST_USER_EMAIL, "other", "other " + TEST_USER_PASSWORD);
		Member me = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		FavoritePath favoritePath1 = new FavoritePath(1L, 1L, 2L);
		FavoritePath favoritePath2 = new FavoritePath(2L, 3L, 4L);
		me.addFavoritePath(favoritePath1);
		me.addFavoritePath(favoritePath2);

		assertThatThrownBy(() -> favoriteService.deletePath(other, 1L))
				.isInstanceOf(NotExistFavoritePathException.class)
				.hasMessage("Id가 1인 즐겨찾기 경로가 존재하지 않습니다.");
	}
}