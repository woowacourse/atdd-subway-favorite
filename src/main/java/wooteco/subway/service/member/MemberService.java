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
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(final StationRepository stationRepository,
        final MemberRepository memberRepository, final JwtTokenProvider jwtTokenProvider) {
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(Member member) {
        Member saved = memberRepository.save(member);
        return MemberResponse.of(saved);
    }

    public void updateMember(Member member, UpdateMemberRequest param) {
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Member member) {
        memberRepository.deleteById(member.getId());
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
            .orElseThrow(RuntimeException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new RuntimeException("잘못된 패스워드");
        }
        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(RuntimeException::new);
    }

    public void addFavorite(final Member member, final FavoriteRequest request) {
        Station source = stationRepository.findById(request.getSource()).orElseThrow(NoSuchElementException::new);
        Station target = stationRepository.findById(request.getTarget()).orElseThrow(NoSuchElementException::new);
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
