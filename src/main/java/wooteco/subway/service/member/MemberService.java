package wooteco.subway.service.member;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberNotfoundException;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public Member createMember(Member member) {
        try {
            return memberRepository.save(member);
        } catch (DbActionExecutionException e) {
            Throwable cause = e.getCause();
            if (Objects.isNull(cause)) {
                throw e;
            }
            throw new CreateMemberException(cause);
        }
    }

    @Transactional
    public void updateMember(Member member, UpdateMemberRequest param) {
        member = findMemberByEmail(member.getEmail());
        if (member.isNotMe(param.getEmail(), param.getPassword())) {
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
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotfoundException(MemberNotfoundException.INVALID_EMAIL_MESSAGE));
    }

    public boolean loginWithForm(String email, String password) {
        Member member = findMemberByEmail(email);
        return member.checkPassword(password);
    }

    @Transactional
    public void removeFavoritePath(Station startStation, Station endStation, Member member) {
        member = findMemberByEmail(member.getEmail());
        member.removeFavoritePath(startStation, endStation);
        memberRepository.save(member);
    }

    public List<FavoritePath> findFavoritePathsOf(Member member) {
        List<FavoritePath> favoritePaths = new ArrayList<>(member.getFavoritePaths());
        return Collections.unmodifiableList(favoritePaths);
    }

    @Transactional
    public void addFavoritePath(Member member, FavoritePath favoritePath) {
        member = findMemberByEmail(member.getEmail());
        member.addFavoritePath(favoritePath);
        memberRepository.save(member);
    }
}
