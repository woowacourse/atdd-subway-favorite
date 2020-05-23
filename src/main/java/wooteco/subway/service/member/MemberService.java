package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.InvalidRegisterException;
import wooteco.subway.web.member.InvalidUpdateException;
import wooteco.subway.web.member.NoSuchMemberException;

@Service
public class MemberService {
    private static final String EMAIL_REG_EXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member createMember(Member member) {
        validateEmailAddress(member.getEmail());
        validateDuplication(member);
        return memberRepository.save(member);
    }

    private void validateEmailAddress(String email) {
        if (!email.matches(EMAIL_REG_EXP)) {
            throw new InvalidRegisterException(InvalidRegisterException.INVALID_EMAIL_FORMAT_MSG);
        }
    }

    private void validateDuplication(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new InvalidRegisterException(InvalidRegisterException.DUPLICATE_EMAIL_MSG);
        }
    }

    public void updateMember(Member member, Long id, UpdateMemberRequest param) {
        Member targetMember = memberRepository.findById(id).orElseThrow(NoSuchMemberException::new);
        if (!member.getEmail().equals(targetMember.getEmail())) {
            throw new InvalidUpdateException();
        }
        targetMember.update(param.getName(), param.getPassword());
        memberRepository.save(targetMember);
    }

    public void updateMember(Member member, UpdateMemberRequest param) {
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Member member, Long id) {
        Member targetMember = memberRepository.findById(id).orElseThrow(NoSuchMemberException::new);
        if (!member.getEmail().equals(targetMember.getEmail())) {
            throw new InvalidUpdateException();
        }
        memberRepository.deleteById(id);
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail()).orElseThrow(NoSuchMemberException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new NoSuchMemberException("잘못된 패스워드");
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(NoSuchMemberException::new);
    }

    public boolean loginWithForm(String email, String password) {
        Member member = findMemberByEmail(email);
        return member.checkPassword(password);
    }
}
