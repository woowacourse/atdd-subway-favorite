package wooteco.subway.service.favorite;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Service
public class FavoriteService {
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationRepository stationRepository,
        FavoriteRepository favoriteRepository) {
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station source = getStationByName(favoriteRequest.getSource());
        Station target = getStationByName(favoriteRequest.getTarget());

        Favorite favorite = Favorite.of(memberId, source.getId(), target.getId());
        return favoriteRepository.save(favorite);
    }

    private Station getStationByName(String stationName) {
        return stationRepository.findByName(stationName)
            .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    public List<FavoriteResponse> getFavoriteResponse(Long id) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(id);

        return favorites.stream()
            .map(favorite -> FavoriteResponse.of(favorite.getId(), getStationById(favorite.getSourceId()).getName(),
                getStationById(favorite.getTargetId()).getName())
            )
            .collect(Collectors.toList());
    }

    private Station getStationById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        favoriteRepository.deleteByMemberIdAndId(memberId, favoriteId);
    }

    public void deleteFavorites(Long memberId) {
        favoriteRepository.deleteAllByMemberId(memberId);
    }
}
