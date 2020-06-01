package wooteco.subway.web.service.member;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.exception.NotFoundMemberException;
import wooteco.subway.web.exception.NotMatchPasswordException;
import wooteco.subway.web.service.member.dto.LoginRequest;
import wooteco.subway.web.service.member.dto.UpdateMemberRequest;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public void updateMember(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundMemberException(param.getName()));
        if (!member.checkPassword(param.getOldPassword())) {
            throw new NotMatchPasswordException();
        }
        member.update(param.getName(), param.getNewPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
            .orElseThrow(() -> new NotFoundMemberException(param.getEmail()));
        if (!member.checkPassword(param.getPassword())) {
            throw new NotMatchPasswordException();
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundMemberException(email));
    }

    @Transactional(readOnly = true)
    public boolean isExistEmail(String email) {
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        return byEmail.isPresent();
    }
}
