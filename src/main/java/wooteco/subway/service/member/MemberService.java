package wooteco.subway.service.member;

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
import wooteco.subway.service.station.StationService;
import wooteco.subway.web.member.InvalidRegisterException;
import wooteco.subway.web.member.InvalidUpdateException;
import wooteco.subway.web.member.NoExistFavoriteException;
import wooteco.subway.web.member.NoExistMemberException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private static final String EMAIL_REG_EXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

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

    public Member createMember(Member member) {
        validateEmailAddress(member.getEmail());
        validateDuplication(member);
        return memberRepository.save(member);
    }

    private void validateEmailAddress(String email) {
        if (!email.matches(EMAIL_REG_EXP)) {
            throw new InvalidRegisterException(InvalidRegisterException.INVALID_EMAIL_FORMAT_MSG);
        }
    }

    private void validateDuplication(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new InvalidRegisterException(InvalidRegisterException.DUPLICATE_EMAIL_MSG);
        }
    }

    public void updateMember(Member member, Long id, UpdateMemberRequest param) {
        Member targetMember = memberRepository.findById(id).orElseThrow(NoExistMemberException::new);
        if (!member.getEmail().equals(targetMember.getEmail())) {
            throw new InvalidUpdateException();
        }
        targetMember.update(param.getName(), param.getPassword());
        memberRepository.save(targetMember);
    }

    public void updateMember(Member member, UpdateMemberRequest param) {
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Member member, Long id) {
        Member targetMember = memberRepository.findById(id).orElseThrow(NoExistMemberException::new);
        if (!member.getEmail().equals(targetMember.getEmail())) {
            throw new InvalidUpdateException();
        }
        memberRepository.deleteById(id);
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail()).orElseThrow(NoExistMemberException::new);
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
        return favorites.stream()
                .map(favorite -> {
                    Station sourceStation = stationService.findStationById(favorite.getSourceStationId());
                    Station targetStation = stationService.findStationById(favorite.getTargetStationId());
                    return new FavoriteResponse(favorite.getId(), favorite.getSourceStationId(), favorite.getTargetStationId(),
                            sourceStation.getName(), targetStation.getName());
                }).collect(Collectors.toList());
    }
}
