package wooteco.subway.service.member;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.station.NoExistStationException;
import wooteco.subway.service.station.StationService;
import wooteco.subway.web.member.InvalidRegisterException;
import wooteco.subway.web.member.InvalidUpdateException;
import wooteco.subway.web.member.NoExistFavoriteException;
import wooteco.subway.web.member.NoExistMemberException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private FavoriteRepository favoriteRepository;
    private JwtTokenProvider jwtTokenProvider;
    private StationService stationService;

    public MemberService(MemberRepository memberRepository, FavoriteRepository favoriteRepository, JwtTokenProvider jwtTokenProvider, StationService stationService) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.stationService = stationService;
    }

    public Member createMember(@Valid Member member) {
        validateDuplication(member);
        try {
            return memberRepository.save(member);
        } catch (DbActionExecutionException e) {
            throw new InvalidRegisterException(InvalidRegisterException.DUPLICATE_EMAIL_MSG);
        }
    }

    private void validateDuplication(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new InvalidRegisterException(InvalidRegisterException.DUPLICATE_EMAIL_MSG);
        }
    }

    public void updateMember(Member member, Long id, UpdateMemberRequest param) {
        Member targetMember = memberRepository.findById(id).orElseThrow(NoExistMemberException::new);
        validateAccessible(member, targetMember);
        targetMember.update(param.getName(), param.getPassword());
        memberRepository.save(targetMember);
    }

    private void validateAccessible(Member member, Member targetMember) {
        if (member.isNotSameUser(targetMember)) {
            throw new InvalidUpdateException();
        }
    }

    public void updateMember(Member member, UpdateMemberRequest param) {
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Member member, Long id) {
        Member targetMember = memberRepository.findById(id).orElseThrow(NoExistMemberException::new);
        validateAccessible(member, targetMember);
        memberRepository.deleteById(id);
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    public String createToken(LoginRequest param) {
        Member member = findMemberByEmail(param.getEmail());
        if (!member.checkPassword(param.getPassword())) {
            throw new NoExistMemberException("잘못된 패스워드");
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(NoExistMemberException::new);
    }

    public void addFavorite(Member member, FavoriteCreateRequest param) {
        Favorite favorite = new Favorite(param.getSource(), param.getTarget());
        member.addFavorite(favorite);
        memberRepository.save(member);
    }

    public void removeFavorite(Member member, Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(NoExistFavoriteException::new);
        member.removeFavorite(favorite);
        memberRepository.save(member);
    }

    public List<FavoriteResponse> findAllFavorites(Member member) {
        Set<Favorite> favorites = member.getFavorites();
        List<Station> stations = stationService.findStations();
        return favorites.stream()
                .map(favorite -> {
                    Station sourceStation = getStationById(stations, favorite.getSourceStationId());
                    Station targetStation = getStationById(stations, favorite.getTargetStationId());
                    return new FavoriteResponse(favorite.getId(), favorite.getSourceStationId(), favorite.getTargetStationId(),
                            sourceStation.getName(), targetStation.getName());
                }).collect(Collectors.toList());
    }

    private Station getStationById(List<Station> stations, Long sourceStationId) {
        return stations.stream()
                .filter(x -> x.isSameId(sourceStationId))
                .findFirst()
                .orElseThrow(NoExistStationException::new);
    }
}
