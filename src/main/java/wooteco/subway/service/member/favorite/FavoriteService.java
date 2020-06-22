package wooteco.subway.service.member.favorite;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.FavoriteDetail;
import wooteco.subway.domain.member.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository,
            StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public Favorite addFavorite(long memberId, long sourceId, long targetId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Optional<Station> source = stationRepository.findById(sourceId);
        Optional<Station> target = stationRepository.findById(targetId);

        if (!member.isPresent() || !source.isPresent() || !target.isPresent()) {
            throw new IllegalArgumentException();
        }

        Favorite favorite = new Favorite(member.get(), source.get(), target.get());
        favoriteRepository.save(favorite);
        return favorite;

        // Member member = memberRepository.findById(memberId)
        //         .orElseThrow(NoMemberExistException::new);
        // member.validateDuplicatedFavorite(sourceId, targetId);
        // Favorite favorite = Favorite.of(sourceId, targetId);
        // member.addFavorite(favorite);
        // memberRepository.save(member);
        //
        // return favorite;
    }

    public List<FavoriteDetail> readFavorites(long memberId) {
        return favoriteRepository.findAllByMemberId(memberId)
                .stream()
                .map(FavoriteDetail::of)
                .collect(toList());

        // Member member = memberRepository.findById(memberId)
        //         .orElseThrow(NoMemberExistException::new);
        //
        // Favorites favorites = member.getFavorites();
        // Set<Long> ids = favorites.extractStationIds();
        // Stations stations = new Stations(stationRepository.findAllById(ids));
        //
        // return favorites.getFavorites().stream()
        //         .map(favorite -> FavoriteDetail.of(
        //                 memberId,
        //                 favorite,
        //                 stations.extractStationById(favorite.getSourceId()).getName(),
        //                 stations.extractStationById(favorite.getTargetId()).getName()))
        //         .collect(Collectors.toList());
    }

    public void removeFavorite(long memberId, long sourceId, long targetId) {
        Optional<Favorite> favorite = favoriteRepository.findByMemberIdAndSourceIdAndTargetId(
                memberId, sourceId, targetId);

        favorite.ifPresent(value -> favoriteRepository.delete(value));

        // Member member = memberRepository.findById(memberId)
        //         .orElseThrow(NoMemberExistException::new);
        //
        // member.removeFavorite(Favorite.of(sourceId, targetId));
        // memberRepository.save(member);
    }
}
