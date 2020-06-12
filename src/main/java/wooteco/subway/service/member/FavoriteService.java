package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.service.exception.SameStationException;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationService stationService;

    public FavoriteService(MemberRepository memberRepository, StationService stationService) {
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    @Transactional
    public void createFavorite(FavoriteRequest favoriteRequest, Member member) {
        validate(favoriteRequest);
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        member.addFavorite(new Favorite(sourceId, destinationId));
        memberRepository.save(member);
    }

    @Transactional
    public void removeFavorite(FavoriteRequest favoriteRequest, Member member) {
        validate(favoriteRequest);
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        member.removeFavorite(new Favorite(sourceId, destinationId));
        memberRepository.save(member);
    }

    private static List<FavoriteResponse> toFavoriteResponses(Stations stations, Favorites favorites) {
        return favorites.getFavorites().stream()
                .map(favorite -> toFavoriteResponse(stations, favorite))
                .collect(Collectors.toList());
    }

    public boolean hasFavorite(FavoriteRequest favoriteRequest, Member member) {
        validate(favoriteRequest);
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        return member.hasFavorite(new Favorite(sourceId, destinationId));
    }

    private void validate(FavoriteRequest favoriteRequest) {
        if (favoriteRequest.getSourceName().equals(favoriteRequest.getDestinationName())) {
            throw new SameStationException();
        }
    }

    private static FavoriteResponse toFavoriteResponse(Stations stations, Favorite favorite) {
        String sourceName = stations.extractStationById(favorite.getSourceId()).getName();
        String destinationName = stations.extractStationById(favorite.getDestinationId()).getName();
        return new FavoriteResponse(sourceName, destinationName);
    }

    public List<FavoriteResponse> showAllFavorites(Member member) {
        Favorites favorites = member.getFavorites();

        return toFavoriteResponses(stationService.findStations(), favorites);
    }
}
