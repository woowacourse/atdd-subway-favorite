package wooteco.subway.service.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

public class FavoriteServiceTest {

    private static final String SAMSEOK = "삼척";
    private static final String DONGSAN = "동산";
    private static final String JAMSIL = "잠실";
    private static final String SEOKCHONGOBUN = "석촌고분";

    private FavoriteService favoriteService;

    private StationRepository stationRepository;

    @BeforeEach
    public void setUp() {
        FavoriteRepository favoriteRepository = new InMemoryFavorite();
        stationRepository = new InMemoryStation();
        favoriteService = new FavoriteService(stationRepository, favoriteRepository);
    }

    @Test
    public void createFavoriteTest() {
        Station samchuck = new Station(SAMSEOK);
        Station dongsan = new Station(DONGSAN);

        stationRepository.save(samchuck);
        stationRepository.save(dongsan);

        FavoriteRequest favoriteRequest = new FavoriteRequest(SAMSEOK, DONGSAN);

        Favorite persistFavorite = favoriteService.createFavorite(1L, favoriteRequest);

        assertThat(persistFavorite).isInstanceOf(Favorite.class);
        assertThat(persistFavorite.getId()).isEqualTo(1L);
        assertThat(persistFavorite.getMemberId()).isEqualTo(1L);
        assertThat(persistFavorite.getSourceId()).isEqualTo(1L);
        assertThat(persistFavorite.getTargetId()).isEqualTo(2L);
    }

    @Test
    public void getFavoriteResponseTest() {
        Station sourceStation = new Station(1L, JAMSIL);
        Station targetStation = new Station(2L, SEOKCHONGOBUN);

        stationRepository.save(sourceStation);
        stationRepository.save(targetStation);

        FavoriteRequest favoriteRequest = new FavoriteRequest(JAMSIL, SEOKCHONGOBUN);
        favoriteService.createFavorite(1L, favoriteRequest);

        List<FavoriteResponse> favorites = favoriteService.getFavoriteResponseByMemberId(1L);

        assertThat(favorites).hasSize(1);
        assertThat(favorites.get(0).getSource()).isEqualTo(JAMSIL);
        assertThat(favorites.get(0).getTarget()).isEqualTo(SEOKCHONGOBUN);
    }
}