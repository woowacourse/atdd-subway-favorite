package wooteco.subway.service.member;

import java.util.List;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteDetail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.member.exception.DuplicateMemberException;
import wooteco.subway.service.member.exception.IncorrectPasswordException;
import wooteco.subway.service.member.exception.NotFoundMemberException;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository,
        JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member createMember(Member member) {
        try {
            return memberRepository.save(member);
        } catch (DbActionExecutionException e) {
            throw new DuplicateMemberException(member.getEmail());
        }
    }

    public void updateMember(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id)
            .orElseThrow(NotFoundMemberException::new);
        updateMember(member, param);
    }

    public void updateMember(Member member, UpdateMemberRequest param) {
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
            .orElseThrow(NotFoundMemberException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new IncorrectPasswordException();
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(NotFoundMemberException::new);
    }

    public void addFavorite(Member member, Favorite favorite) {
        member.addFavorite(favorite);
        memberRepository.save(member);
    }

    public List<FavoriteDetail> getFavorites(Member member) {
        return memberRepository.findFavoritesById(member.getId());
    }

    public void removeFavorite(Member member, Long sourceId, Long targetId) {
        member.removeFavorite(new Favorite(sourceId, targetId));
        memberRepository.save(member);
    }

    public boolean hasFavorite(Member member, Long sourceId, Long targetId) {
        return member.hasFavorite(new Favorite(sourceId, targetId));
    }
}
