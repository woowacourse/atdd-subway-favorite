package wooteco.subway.service.member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {
    static final String NOT_MANAGED_BY_REPOSITORY = "repository에 영속되지 않는 member 객체를 update 할 수 없습니다.";
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member createMember(Member member) {
        try {
            return memberRepository.save(member);
        } catch (DbActionExecutionException e) {
            throw new CreateMemberException(CreateMemberException.WRONG_CREATE_MESSAGE);
        }
    }

    public void updateMember(Member member, UpdateMemberRequest param) {
        validatePersistence(member);
        if (member.isNotMe(param.getEmail())) {
            throw new IllegalArgumentException(Member.NOT_ME_MESSAGE);
        }
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail()).orElseThrow(RuntimeException::new);

        if (!member.checkPassword(param.getPassword())) {
            throw new RuntimeException("잘못된 패스워드");
        }
        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    public boolean loginWithForm(String email, String password) {
        Member member = findMemberByEmail(email);
        return member.checkPassword(password);
    }

    public void removeFavoritePath(Station startStation, Station endStation, Member member) {
        validatePersistence(member);
        member.removeFavoritePath(startStation, endStation);
        memberRepository.save(member);
    }

    public List<FavoritePath> findFavoritePathsOf(Member member) {
        List<FavoritePath> favoritePaths = new ArrayList<>(member.getFavoritePaths());
        return Collections.unmodifiableList(favoritePaths);
    }

    public void addFavoritePath(Member member, FavoritePath favoritePath) {
        member.addFavoritePath(favoritePath);
        validatePersistence(member);
        memberRepository.save(member);
    }

    private void validatePersistence(Member member) {
        if (member.isNotPersistent()) {
            throw new IllegalArgumentException(NOT_MANAGED_BY_REPOSITORY);
        }
    }
}
