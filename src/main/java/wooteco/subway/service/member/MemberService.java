package wooteco.subway.service.member;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.NotFoundUserException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository,
        StationRepository stationRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public Member createMember(MemberRequest memberRequest) {
        if (memberRepository.existsByEmail(memberRequest.getEmail())) {
            throw new DuplicateKeyException("존재하는 이메일입니다.");
        }
        return memberRepository.save(memberRequest.toMember());
    }

    @Transactional
    public void updateMember(String email, Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        if (!email.equals(member.getEmail())) {
            throw new IllegalAccessError("잘못된 접근입니다.");
        }
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
            .orElseThrow(NotFoundUserException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new IllegalArgumentException("잘못된 패스워드 입니다.");
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(NotFoundUserException::new);
    }

    public void addFavorite(Member member, FavoriteRequest favoriteRequest) {
        Station source = stationRepository.findByName(favoriteRequest.getSource())
            .orElseThrow(() -> new NoSuchElementException("역을 찾을 수 없습니다."));
        Station target = stationRepository.findByName(favoriteRequest.getTarget())
            .orElseThrow(() -> new NoSuchElementException("역을 찾을 수 없습니다."));
        Favorite favorite = new Favorite(source.getId(), target.getId());

        member.addFavorite(favorite);

        memberRepository.save(member);
    }

    public Set<FavoriteResponse> findFavorites(Member member) {
        Set<Favorite> favorites = member.getFavorites();
        Set<FavoriteResponse> favoriteResponses = new LinkedHashSet<>();
        List<Station> stations = stationRepository.findAll();

        for (Favorite favorite : favorites) {
            FavoriteResponse favoriteResponse = new FavoriteResponse(
                favorite.getId(),
                findStationById(stations, favorite.getSourceStationId()).getName(),
                findStationById(stations, favorite.getTargetStationId()).getName());
            favoriteResponses.add(favoriteResponse);
        }
        return favoriteResponses;
    }

    public void deleteFavorites(Long favoriteId, Member member) {
        member.deleteFavoriteBy(favoriteId);
        memberRepository.save(member);
    }

    private Station findStationById(List<Station> stations, Long Id) {
        return stations.stream()
            .filter(station -> Objects.equals(id, station.getId()))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당 역을 찾을 수 없습니다.")).getName(),
    }
}
