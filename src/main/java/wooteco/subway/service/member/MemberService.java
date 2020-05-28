package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.DuplicatedEmailException;
import wooteco.subway.exception.InvalidAuthenticationException;
import wooteco.subway.exception.NoMemberExistException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

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
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new DuplicatedEmailException();
        }
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(NoMemberExistException::new);
    }

    @Transactional
    public void updateMember(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NoMemberExistException::new);
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
                .orElseThrow(NoMemberExistException::new);
        if (!member.isInvalidPassword(param.getPassword())) {
            throw new InvalidAuthenticationException("잘못된 패스워드에요.");
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    @Transactional(readOnly = true)
    public Member loginWithForm(String email, String password) {
        Member member = findMemberByEmail(email);
        if (!member.isInvalidPassword(password)) {
            throw new InvalidAuthenticationException("비밀번호가 틀렸어요.");
        }
        return member;
    }
}
