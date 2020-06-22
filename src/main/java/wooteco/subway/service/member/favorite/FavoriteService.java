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
import wooteco.subway.exception.DuplicatedFavoriteException;
import wooteco.subway.exception.notexist.NoFavoriteExistException;
import wooteco.subway.exception.notexist.NoMemberExistException;
import wooteco.subway.exception.notexist.NoStationExistsException;

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
        Member member = validateMemberId(memberId);
        Station source = validateStationId(sourceId);
        Station target = validateStationId(targetId);

        validateNoDuplication(memberId, sourceId, targetId);

        return favoriteRepository.save(new Favorite(member, source, target));
    }

    private Member validateMemberId(long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);

        if (!member.isPresent()) {
            throw new NoMemberExistException();
        }
        return member.get();
    }

    private Station validateStationId(long stationId) {
        Optional<Station> station = stationRepository.findById(stationId);

        if (!station.isPresent()) {
            throw new NoStationExistsException();
        }
        return station.get();
    }

    private void validateNoDuplication(long memberId, long sourceId, long targetId) {
        Optional<Favorite> favorite = favoriteRepository.findByMemberIdAndSourceIdAndTargetId(
                memberId, sourceId, targetId);

        if (favorite.isPresent()) {
            throw new DuplicatedFavoriteException();
        }
    }

    public List<FavoriteDetail> readFavorites(long memberId) {
        validateMemberId(memberId);

        return favoriteRepository.findAllByMemberId(memberId)
                .stream()
                .map(FavoriteDetail::of)
                .collect(toList());
    }

    public void removeFavorite(long memberId, long sourceId, long targetId) {
        Optional<Favorite> favorite = favoriteRepository.findByMemberIdAndSourceIdAndTargetId(
                memberId, sourceId, targetId);

        if (!favorite.isPresent()) {
            throw new NoFavoriteExistException();
        }
        favoriteRepository.delete(favorite.get());
    }
}
