package wooteco.subway.service.member;

import java.util.Optional;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.NotFoundMemberException;
import wooteco.subway.web.member.NotMatchPasswordException;

@Service
public class MemberService {
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
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!member.checkPassword(param.getOldPassword())) {
            throw new NotMatchPasswordException("패스워드가 일치하지 않습니다");
        }
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
            .orElseThrow(() -> new NotFoundMemberException(param.getEmail()));
        if (!member.checkPassword(param.getPassword())) {
            throw new RuntimeException("잘못된 패스워드 입니다.");
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    public boolean isNotExistEmail(String email) {
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        return !byEmail.isPresent();
    }
}
