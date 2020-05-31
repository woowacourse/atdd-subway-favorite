package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.CustomException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import java.util.NoSuchElementException;

@Service
public class MemberService {
    private static final String NO_SUCH_MEMBER_MESSAGE = "존재하지 않는 사용자입니다.";

    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public void updateMember(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(new NoSuchElementException(NO_SUCH_MEMBER_MESSAGE)));
        updateMember(member, param);
    }

    public void updateMember(Member member, UpdateMemberRequest param) {
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public void deleteMember(Member member) {
        deleteMember(member.getId());
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
                .orElseThrow(() -> new CustomException(new NoSuchElementException(NO_SUCH_MEMBER_MESSAGE)));
        if (!member.checkPassword(param.getPassword())) {
            throw new CustomException(new IllegalArgumentException("잘못된 패스워드입니다."));
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(new NoSuchElementException(NO_SUCH_MEMBER_MESSAGE)));
    }
}
