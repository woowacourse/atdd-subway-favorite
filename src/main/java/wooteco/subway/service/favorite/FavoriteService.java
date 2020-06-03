package wooteco.subway.service.favorite;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.favorite.Favorites;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.exception.DuplicateFavoriteException;
import wooteco.subway.service.favorite.exception.NoExistFavoriteException;
import wooteco.subway.service.member.exception.InvalidMemberIdException;
import wooteco.subway.service.station.exception.InvalidStationNameException;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository,
        FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Long create(Long memberId, FavoriteRequest favoriteRequest) {
        memberRepository.findById(memberId).orElseThrow(InvalidMemberIdException::new);
        Station departure = stationRepository.findByName(favoriteRequest.getDeparture())
            .orElseThrow(InvalidStationNameException::new);
        Station arrival = stationRepository.findByName(favoriteRequest.getArrival())
            .orElseThrow(InvalidStationNameException::new);

        Favorite favorite = Favorite.of(memberId, departure, arrival);

        if (isDuplicate(memberId, favorite)) {
            throw new DuplicateFavoriteException();
        }

        return favoriteRepository.save(favorite).getId();
    }

    private boolean isDuplicate(Long memberId, Favorite favorite) {
        return favoriteRepository.findAllByMemberId(memberId)
            .stream()
            .anyMatch(f -> f.isDuplicate(favorite));
    }

    @Transactional
    public void delete(Long memberId, FavoriteRequest favoriteRequest) {
        Station departure = stationRepository.findByName(favoriteRequest.getDeparture())
            .orElseThrow(InvalidStationNameException::new);
        Station arrival = stationRepository.findByName(favoriteRequest.getArrival())
            .orElseThrow(InvalidStationNameException::new);
        Favorite favorite = favoriteRepository.findByMemberIdAndDepartureStationIdAndArrivalStationId(
            memberId, departure.getId(), arrival.getId())
            .orElseThrow(NoExistFavoriteException::new);

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(Long memberId) {
        Favorites favorites = new Favorites(favoriteRepository.findAllByMemberId(memberId));
        List<Station> departures = stationRepository.findAllById(
            favorites.getDepartureStationIds());
        List<Station> arrivals = stationRepository.findAllById(favorites.getArrivalStationIds());
        return FavoriteResponse.listOf(departures, arrivals);
    }

    @Transactional
    public void deleteByMemberId(Long memberId) {
        favoriteRepository.deleteAllByMemberId(memberId);
    }

    public void deleteByStationId(Long stationId) {
        favoriteRepository.deleteAllByStationId(stationId);
    }
}
