package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.exception.DuplicateFavoriteException;
import wooteco.subway.service.favorite.exception.NoExistFavoriteException;
import wooteco.subway.service.member.exception.InvalidMemberIdException;
import wooteco.subway.service.station.exception.InvalidStationNameException;

import java.util.List;

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
        stationRepository.findById(favoriteRequest.getDepartureId()).orElseThrow(InvalidStationNameException::new);
        stationRepository.findById(favoriteRequest.getArrivalId()).orElseThrow(InvalidStationNameException::new);

        Favorite favorite = Favorite.of(memberId, favoriteRequest);

        if (isDuplicateFavorite(memberId, favorite)) {
            throw new DuplicateFavoriteException();
        }

        return favoriteRepository.save(favorite).getId();
    }

    private boolean isDuplicateFavorite(Long memberId, Favorite favorite) {
        return favoriteRepository.findAllByMemberId(memberId)
                .stream()
                .anyMatch(f -> f.isDuplicate(favorite));
    }

    @Transactional
    public void delete(Long memberId, FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteRepository.findByMemberIdAndDepartureIdAndArrivalId(memberId,
                favoriteRequest.getDepartureId(), favoriteRequest.getArrivalId())
                .orElseThrow(NoExistFavoriteException::new);

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return FavoriteResponse.listOf(favorites);
    }
}
