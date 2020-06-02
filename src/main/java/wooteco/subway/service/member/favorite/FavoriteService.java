package wooteco.subway.service.member.favorite;


import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.*;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exception.notexist.NoMemberExistException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public Favorite addFavorite(long memberId, long sourceId, long targetId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoMemberExistException::new);
        member.validateDuplicatedFavorite(sourceId, targetId);
        Favorite favorite = Favorite.of(sourceId, targetId);
        member.addFavorite(favorite);
        memberRepository.save(member);

        return favorite;
    }

    public List<FavoriteDetail> readFavorites(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoMemberExistException::new);

        Favorites favorites = member.getFavorites();
        Set<Long> ids = favorites.extractStationIds();
        Stations stations = new Stations(stationRepository.findAllById(ids));

        return favorites.getFavorites().stream()
                .map(favorite -> FavoriteDetail.of(
                        memberId,
                        favorite,
                        stations.extractStationById(favorite.getSourceId()).getName(),
                        stations.extractStationById(favorite.getTargetId()).getName()))
                .collect(Collectors.toList());
    }

    public void removeFavorite(long memberId, long sourceId, long targetId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoMemberExistException::new);

        member.removeFavorite(Favorite.of(sourceId, targetId));
        memberRepository.save(member);
    }
}
