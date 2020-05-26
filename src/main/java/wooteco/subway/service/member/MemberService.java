package wooteco.subway.service.member;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.NoSuchAccountException;
import wooteco.subway.exception.NoSuchStationException;
import wooteco.subway.exception.WrongPasswordException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository,
        StationRepository stationRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member createMember(Member member) {
        try {
            return memberRepository.save(member);
        } catch (DbActionExecutionException e) {
            throw new DuplicateEmailException();
        }
    }

    public void updateMember(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
            .orElseThrow(NoSuchAccountException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new WrongPasswordException();
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(NoSuchAccountException::new);
    }

    public List<FavoriteResponse> findAllFavoritesByMember(Member member) {
        Map<Long, String> stationNameById = stationRepository.findAll().stream()
            .collect(Collectors.toMap(Station::getId, Station::getName));

        return member.getFavorites().stream()
            .map(favorite ->
                new FavoriteResponse(favorite.getId(),
                    stationNameById.get(favorite.getSourceStationId()),
                    stationNameById.get(favorite.getTargetStationId()))
            ).collect(Collectors.toList());
    }

    public Member saveFavorite(Long id, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchAccountException::new);
        member.addFavorite(Favorite.of(favoriteRequest));
        return memberRepository.save(member);
    }

    public void deleteFavorite(Member member, Long favoriteId) {
        if (member.doesNotHaveFavoriteWithId(favoriteId)) {
            throw new IllegalArgumentException("잘못된 즐겨찾기 삭제 요청입니다.");
        }
        memberRepository.deleteFavoriteById(favoriteId);
    }

    public String findStationNameById(final Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(NoSuchStationException::new)
            .getName();
    }
}
