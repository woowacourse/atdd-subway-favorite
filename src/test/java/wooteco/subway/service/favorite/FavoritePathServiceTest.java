package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static wooteco.subway.AcceptanceTest.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import wooteco.subway.domain.path.FavoritePathRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exceptions.DuplicatedFavoritePathException;
import wooteco.subway.exceptions.NotExistFavoritePathException;
import wooteco.subway.service.favorite.dto.FavoritePathResponse;

@ExtendWith(MockitoExtension.class)
class FavoritePathServiceTest {
    private Member member;
    private Station kangnam;
    private Station hanti;
    private Station dogok;
    private Station yangjae;

    private FavoritePathService favoritePathService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private FavoritePathRepository favoritePathRepository;

    @BeforeEach
    void setUp() {
        this.favoritePathService = new FavoritePathService(stationRepository, favoritePathRepository);
        member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        kangnam = new Station(1L, STATION_NAME_KANGNAM);
        hanti = new Station(2L, STATION_NAME_HANTI);
        dogok = new Station(3L, STATION_NAME_DOGOK);
        yangjae = new Station(4L, STATION_NAME_YANGJAE);
    }

    @DisplayName("즐겨찾기 경로 등록에 성공하는지 확인")
    @Test
    void registerPath() {
        FavoritePath expectedPath = new FavoritePath(1L, kangnam.getId(), hanti.getId(), member.getId());

        BDDMockito.when(stationRepository.findByName(kangnam.getName())).thenReturn(Optional.of(kangnam));
        BDDMockito.when(stationRepository.findByName(hanti.getName())).thenReturn(Optional.of(hanti));
        BDDMockito.when(favoritePathRepository.findByUniqueField(anyLong(), anyLong(), anyLong()))
            .thenReturn(Optional.empty());
        BDDMockito.when(favoritePathRepository.save(new FavoritePath(kangnam.getId(), hanti.getId(), member.getId())))
            .thenReturn(expectedPath);

        FavoritePath favoritePath = favoritePathService.registerPath(member, kangnam.getName(), hanti.getName());

        assertThat(favoritePath.getId()).isEqualTo(1L);
        assertThat(favoritePath.getSourceId()).isEqualTo(kangnam.getId());
        assertThat(favoritePath.getTargetId()).isEqualTo(hanti.getId());
        assertThat(favoritePath.getMemberId()).isEqualTo(member.getId());
    }

    @DisplayName("이미 등록된 즐겨찾기 경로를 재등록 시 실패하는지 확인")
    @Test
    void registerPathFailedWhenDuplicatedRegister() {
        BDDMockito.when(stationRepository.findByName(kangnam.getName())).thenReturn(Optional.of(kangnam));
        BDDMockito.when(stationRepository.findByName(hanti.getName())).thenReturn(Optional.of(hanti));
        BDDMockito.when(favoritePathRepository.findByUniqueField(anyLong(), anyLong(), anyLong()))
            .thenReturn(Optional.of(new FavoritePath(1L, kangnam.getId(), hanti.getId())));

        assertThatThrownBy(() -> favoritePathService.registerPath(member, kangnam.getName(), hanti.getName()))
            .isInstanceOf(DuplicatedFavoritePathException.class)
            .hasMessage("이미 등록된 즐겨찾기 경로입니다!");
    }

    @DisplayName("즐겨찾기 경로 조회에 성공하는지 확인")
    @Test
    void retrievePath() {
        FavoritePath favoritePath1 = new FavoritePath(1L, kangnam.getId(), hanti.getId(), member.getId());
        FavoritePath favoritePath2 = new FavoritePath(2L, dogok.getId(), yangjae.getId(), member.getId());

        BDDMockito.when(favoritePathRepository.findAllByMemberId(member.getId()))
            .thenReturn(Arrays.asList(favoritePath1, favoritePath2));
        BDDMockito.when(stationRepository.findAllById(any())).thenReturn(Arrays.asList(kangnam, hanti, dogok, yangjae));

        List<FavoritePathResponse> favoritePathResponse = favoritePathService.retrievePath(member);

        assertThat(favoritePathResponse).hasSize(2);
        assertThat(favoritePathResponse.get(0).getId()).isNotNull();
        assertThat(favoritePathResponse.get(0).getSource().getName()).isEqualTo(STATION_NAME_KANGNAM);
    }

    @DisplayName("즐겨찾기 경로 삭제에 성공하는지 확인")
    @Test
    void deletePath() {
        FavoritePath favoritePath = new FavoritePath(1L, kangnam.getId(), hanti.getId(), member.getId());

        BDDMockito.when(favoritePathRepository.findByMemberIdAndPathId(member.getId(), favoritePath.getId()))
            .thenReturn(Optional.of(favoritePath));

        favoritePathService.deletePath(member, 1L);

        verify(favoritePathRepository).delete(favoritePath);
    }

    @DisplayName("다른 회원의 즐겨찾기 경로 삭제 시도 시 실패하는지 확인")
    @Test
    void deletePathFailedWhenDeleteNotMyPath() {
        Member other = new Member(2L, "other " + TEST_USER_EMAIL, "other", "other " + TEST_USER_PASSWORD);
        FavoritePath favoritePath = new FavoritePath(1L, kangnam.getId(), hanti.getId(), member.getId());

        BDDMockito.when(favoritePathRepository.findByMemberIdAndPathId(other.getId(), favoritePath.getId()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> favoritePathService.deletePath(other, 1L))
            .isInstanceOf(NotExistFavoritePathException.class)
            .hasMessage("Id가 1인 즐겨찾기 경로가 존재하지 않습니다.");
    }
}