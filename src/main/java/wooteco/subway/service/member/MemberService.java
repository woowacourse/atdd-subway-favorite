package wooteco.subway.service.member;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.member.LoginEmail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.exception.DuplicatedEmailException;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.dto.ErrorCode;
import wooteco.subway.web.member.exception.MemberException;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public Long createMember(MemberRequest memberRequest) {
        Member member = new Member(memberRequest.getEmail(), memberRequest.getName(), memberRequest.getPassword());
        validateName(member);
        try {
            return memberRepository.save(member).getId();
        } catch (DuplicateKeyException e) {
            throw new DuplicatedEmailException(member.getEmail());
        }
    }

    private void validateName(final Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new DuplicatedEmailException(member.getEmail());
        }
    }

    @Transactional
    public void updateMember(UpdateMemberRequest param, LoginEmail loginEmail) {
        Member member = getMember(loginEmail.getEmail());
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    @Transactional
    public String createToken(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        Member member = getMember(email);
        if (!member.checkPassword(loginRequest.getPassword())) {
            throw new MemberException(ErrorCode.WRONG_PASSWORD);
        }

        return jwtTokenProvider.createToken(loginRequest.getEmail());
    }

    private Member getMember(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(String.format("%s : 가입하지 않은 이메일입니다.", email), ErrorCode.UNSIGNED_EMAIL));
    }

    @Transactional
    public Member findMemberByEmail(LoginEmail loginEmail) {
        return getMember(loginEmail.getEmail());
    }

    @Transactional
    public void deleteByEmail(final LoginEmail loginEmail) {
        Member member = getMember(loginEmail.getEmail());
        memberRepository.delete(member);
    }
}
