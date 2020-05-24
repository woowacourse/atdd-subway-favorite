package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.List;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(final FavoriteRepository favoriteRepository, final StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse addFavorite(final Long memberId, final FavoriteRequest request) {
        Long sourceStationId = stationRepository.findStationIdByName(request.getSource());
        Long targetStationId = stationRepository.findStationIdByName(request.getTarget());

        Favorite favorite = favoriteRepository.save(request.toFavorite(memberId, sourceStationId, targetStationId));

        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> showMyAllFavorites(final Long memberId) {
        return FavoriteResponse.listFrom(favoriteRepository.findAllByMemberId(memberId));
    }
}
