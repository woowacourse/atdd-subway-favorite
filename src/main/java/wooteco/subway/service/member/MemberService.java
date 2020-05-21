package wooteco.subway.service.member;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.LoginEmail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.exception.DuplicatedEmailException;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

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

    public void updateMember(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public String createToken(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.getEmail()).orElseThrow(RuntimeException::new);
        if (!member.checkPassword(loginRequest.getPassword())) {
            throw new RuntimeException("잘못된 패스워드");
        }

        return jwtTokenProvider.createToken(loginRequest.getEmail());
    }

    public Member findMemberByEmail(String email, LoginEmail loginEmail) {
        loginEmail.validate(email);
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    public Member findMemberByEmail(LoginEmail loginEmail) {
        return memberRepository.findByEmail(loginEmail.getEmail()).orElseThrow(RuntimeException::new);
    }

}
