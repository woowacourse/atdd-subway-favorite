package wooteco.subway.service.member;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@Service
public class FavoriteService {
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(StationRepository stationRepository, MemberRepository memberRepository) {
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    public void addFavorite(final Member member, final FavoriteRequest request) {
        Station source = stationRepository.findById(request.getSource())
                .orElseThrow(NoSuchElementException::new);
        Station target = stationRepository.findById(request.getTarget())
                .orElseThrow(NoSuchElementException::new);
        member.addFavorite(new Favorite(source.getId(), target.getId()));
        memberRepository.save(member);
    }

    public List<FavoriteResponse> getAllFavorites(final Member member) {
        Favorites favorites = member.getFavorites();
        List<Station> stations = stationRepository.findAllById(favorites.getAllStations());

        return favorites.getFavorites()
                .stream()
                .map(favorite -> FavoriteResponse.of(favorite, stations))
                .collect(Collectors.toList());
    }

    public void removeFavoriteById(final Member member, final Long id) {
        member.removeFavorite(id);
        memberRepository.save(member);
    }
}
