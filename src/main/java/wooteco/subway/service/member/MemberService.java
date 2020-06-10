package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.InvalidAuthenticationException;

import static wooteco.subway.service.constants.ErrorMessage.*;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository,
                         JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public Member createMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(it -> {
                    throw new IllegalArgumentException(ALREADY_EXIST_MEMBER);
                });

        return memberRepository.save(member);
    }

    @Transactional
    public void updateMember(Member member, UpdateMemberRequest param) {
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    @Transactional
    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
                .orElseThrow(() -> new InvalidAuthenticationException(NO_MEMBER));
        if (!member.checkPassword(param.getPassword())) {
            throw new RuntimeException(WRONG_PASSWORD);
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MEMBER));
    }
}
