package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_EMAIL;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_NAME;
import static wooteco.subway.service.member.MemberServiceTest.TEST_USER_PASSWORD;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.dto.FavoriteDeleteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@ExtendWith(MockitoExtension.class)
public class FavoritesServiceTest {
    private Member MEMBER_BROWN;

    private FavoritesService favoritesService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        favoritesService = new FavoritesService(memberRepository, stationRepository);
        MEMBER_BROWN = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    void addFavorite() {
        BDDMockito.given(stationRepository.findAllById(anyList()))
                .willReturn(Arrays.asList(new Station(1L, "서울"), new Station(2L, "강남"), new Station(3L, "잠실")));

        favoritesService.addFavorite(MEMBER_BROWN, new FavoriteCreateRequest(1L, 2L));

        List<FavoriteResponse> favorites = favoritesService.getFavorites(MEMBER_BROWN);
        assertThat(favorites.size()).isEqualTo(1);
    }

    @Test
    void getFavorites() {
        BDDMockito.given(stationRepository.findAllById(anyList()))
                .willReturn(Arrays.asList(new Station(1L, "서울"), new Station(2L, "강남"), new Station(3L, "잠실")));
        MEMBER_BROWN.addFavorite(new Favorite(1L, 2L));
        MEMBER_BROWN.addFavorite(new Favorite(2L, 3L));

        List<FavoriteResponse> favorites = favoritesService.getFavorites(MEMBER_BROWN);

        assertThat(favorites.size()).isEqualTo(2);
    }

    @Test
    void deleteFavorite() {
        MEMBER_BROWN.addFavorite(new Favorite(1L, 2L));
        MEMBER_BROWN.addFavorite(new Favorite(2L, 3L));

        favoritesService.deleteFavorite(MEMBER_BROWN, new FavoriteDeleteRequest(1L, 2L));

        List<FavoriteResponse> favorites = favoritesService.getFavorites(MEMBER_BROWN);

        assertThat(favorites.size()).isEqualTo(1);
    }
}
