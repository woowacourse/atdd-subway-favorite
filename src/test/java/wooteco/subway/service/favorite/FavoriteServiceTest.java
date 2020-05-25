package wooteco.subway.service.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.acceptance.favorite.dto.StationPathResponse;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.path.StationPath;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exceptions.DuplicatedFavoritePathException;
import wooteco.subway.exceptions.NotExistFavoritePathException;
import wooteco.subway.service.FavoriteService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
	private FavoriteService favoriteService;

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private StationRepository stationRepository;

	@BeforeEach
	void setUp() {
		this.favoriteService = new FavoriteService(memberRepository, stationRepository);
	}

	@DisplayName("즐겨찾기 경로 등록에 성공하는지 확인")
	@Test
	void registerPath() {
		Member expectedMember = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Station source = new Station(1L, STATION_NAME_KANGNAM);
		Station target = new Station(2L, STATION_NAME_HANTI);
		StationPath stationPathToAdd = new StationPath(1L, 1L, 2L);
		expectedMember.addFavoritePath(stationPathToAdd);

		BDDMockito.when(stationRepository.findByName(STATION_NAME_KANGNAM)).thenReturn(Optional.of(source));
		BDDMockito.when(stationRepository.findByName(STATION_NAME_HANTI)).thenReturn(Optional.of(target));
		BDDMockito.when(memberRepository.save(any())).thenReturn(expectedMember);

		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		StationPath stationPath = favoriteService.registerPath(member, STATION_NAME_KANGNAM, STATION_NAME_HANTI);

		assertThat(stationPath.getId()).isEqualTo(1L);
		assertThat(stationPath.getSourceId()).isEqualTo(1L);
		assertThat(stationPath.getTargetId()).isEqualTo(2L);
	}

	@DisplayName("이미 등록된 즐겨찾기 경로를 재등록 시 실패하는지 확인")
	@Test
	void registerPathFailedWhenDuplicatedRegister() {
		Member expectedMember = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Station source = new Station(1L, STATION_NAME_KANGNAM);
		Station target = new Station(2L, STATION_NAME_HANTI);
		StationPath stationPathToAdd = new StationPath(1L, 1L, 2L);
		expectedMember.addFavoritePath(stationPathToAdd);

		BDDMockito.when(stationRepository.findByName(STATION_NAME_KANGNAM)).thenReturn(Optional.of(source));
		BDDMockito.when(stationRepository.findByName(STATION_NAME_HANTI)).thenReturn(Optional.of(target));

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
		StationPath stationPath1 = new StationPath(1L, 1L, 2L);
		StationPath stationPath2 = new StationPath(2L, 3L, 4L);
		member.addFavoritePath(stationPath1);
		member.addFavoritePath(stationPath2);

		BDDMockito.when(stationRepository.findById(kangnam.getId())).thenReturn(Optional.of(kangnam));
		BDDMockito.when(stationRepository.findById(hanti.getId())).thenReturn(Optional.of(hanti));
		BDDMockito.when(stationRepository.findById(dogok.getId())).thenReturn(Optional.of(dogok));
		BDDMockito.when(stationRepository.findById(yangjae.getId())).thenReturn(Optional.of(yangjae));

		List<StationPathResponse> stationPathResponses = favoriteService.retrievePath(member);

		assertThat(stationPathResponses).hasSize(2);
		assertThat(stationPathResponses.get(0).getId()).isNotNull();
		assertThat(stationPathResponses.get(0).getSource().getName()).isEqualTo(STATION_NAME_KANGNAM);
	}

	@DisplayName("즐겨찾기 경로 삭제에 성공하는지 확인")
	@Test
	void deletePath() {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		StationPath stationPath1 = new StationPath(1L, 1L, 2L);
		StationPath stationPath2 = new StationPath(2L, 3L, 4L);
		member.addFavoritePath(stationPath1);
		member.addFavoritePath(stationPath2);

		favoriteService.deletePath(member, 1L);

		assertThat(member.getFavoritePaths()).hasSize(1);
		assertThat(member.getFavoritePaths().get(0).getSourceId()).isEqualTo(3L);
	}

	@DisplayName("다른 회원의 즐겨찾기 경로 삭제 시도 시 실패하는지 확인")
	@Test
	void deletePathFailedWhenDeleteNotMyPath() {
		Member other = new Member(2L, "other " + TEST_USER_EMAIL, "other", "other " + TEST_USER_PASSWORD);
		Member me = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		StationPath stationPath1 = new StationPath(1L, 1L, 2L);
		StationPath stationPath2 = new StationPath(2L, 3L, 4L);
		me.addFavoritePath(stationPath1);
		me.addFavoritePath(stationPath2);

		assertThatThrownBy(() -> favoriteService.deletePath(other, 1L))
				.isInstanceOf(NotExistFavoritePathException.class)
				.hasMessage("Id가 1인 즐겨찾기 경로가 존재하지 않습니다.");
	}
}