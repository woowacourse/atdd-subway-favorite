package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@SpringBootTest
public class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteService favoriteService;

    @Test
    void delete() {
        Station jamsil = new Station("잠실");
        Station seokcheon = new Station("석촌고분");

        stationRepository.save(jamsil);
        stationRepository.save(seokcheon);

        FavoriteRequest favoriteRequest = new FavoriteRequest("잠실", "석촌고분");

        favoriteService.createFavorite(1L, favoriteRequest);

        Station sourceStation = stationRepository.findByName("잠실")
            .orElseThrow(() -> new IllegalArgumentException(""));
        Station targetStation = stationRepository.findByName("석촌고분")
            .orElseThrow(() -> new IllegalArgumentException(""));

        assertThat(sourceStation.getFavoritesSource()).hasSize(1);
        assertThat(targetStation.getFavoritesTarget()).hasSize(1);

        stationRepository.delete(jamsil);

        List<FavoriteResponse> favoriteResponse = favoriteService.getFavoriteResponse(1L);

        assertThat(favoriteResponse).hasSize(0);
    }
}
