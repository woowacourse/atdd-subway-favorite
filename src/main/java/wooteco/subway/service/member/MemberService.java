package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.DuplicateMemberException;
import wooteco.subway.exception.InvalidAuthenticationException;
import wooteco.subway.exception.NoSuchMemberException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, FavoriteRepository favoriteRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member createMember(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new DuplicateMemberException();
        }
        return memberRepository.save(member);
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(NoSuchMemberException::new);
    }

    public void updateMember(Member member, UpdateMemberRequest param) {
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void updateMemberById(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchMemberException::new);
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Member member) {
        memberRepository.deleteById(member.getId());
        favoriteRepository.deleteAllByMemberId(member.getId());
    }

    public void deleteMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchMemberException::new);
        deleteMember(member);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail()).orElseThrow(RuntimeException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new InvalidAuthenticationException("잘못된 패스워드");
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }
}
