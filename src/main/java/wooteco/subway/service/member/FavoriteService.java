package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.exception.SameStationException;
import wooteco.subway.service.exception.WrongStationException;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationService stationService;

    public FavoriteService(MemberRepository memberRepository, StationService stationService) {
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    public void createFavorite(FavoriteRequest favoriteRequest, Member member) {
        validate(favoriteRequest);
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        member.addFavorite(new Favorite(sourceId, destinationId));
        memberRepository.save(member);
    }

    public void removeFavorite(FavoriteRequest favoriteRequest, Member member) {
        validate(favoriteRequest);
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        member.removeFavorite(Favorite.of(sourceId, destinationId));
        memberRepository.save(member);
    }

    public List<FavoriteResponse> showAllFavorites(Member member) {
        Favorites favorites = member.getFavorites();
        return toFavoriteResponses(favorites, stationService.findStations());
    }

    public boolean existsFavorite(FavoriteRequest favoriteRequest, Member member) {
        validate(favoriteRequest);
        Favorites favorites = member.getFavorites();
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        return favorites.findById(sourceId, destinationId).isPresent();
    }

    private void validate(FavoriteRequest favoriteRequest) {
        if (favoriteRequest.getSourceName().equals(favoriteRequest.getDestinationName())) {
            throw new SameStationException();
        }
    }

    public List<FavoriteResponse> toFavoriteResponses(Favorites favorites, List<Station> stations) {
        return favorites.getFavorites().stream()
                .map(favorite -> toFavoriteResponse(stations, favorite))
                .collect(Collectors.toList());
    }

    private FavoriteResponse toFavoriteResponse(List<Station> stations, Favorite favorite) {
        String sourceName = getStationName(stations, favorite.getSourceId());
        String destinationName = getStationName(stations, favorite.getDestinationId());
        return new FavoriteResponse(sourceName, destinationName);
    }

    private String getStationName(List<Station> stations, Long sourceId) {
        return stations.stream()
                .filter(station -> station.getId().equals(sourceId))
                .map(Station::getName)
                .findFirst()
                .orElseThrow(WrongStationException::new);
    }
}
