package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.exception.InvalidPasswordException;
import wooteco.subway.web.member.exception.NotExistMemberDataException;

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
        return memberRepository.save(member);
    }

    @Transactional
    public void updateMember(Member member, UpdateMemberRequest param) {
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional
    public String createToken(LoginRequest param) throws InvalidPasswordException {
        Member member = memberRepository.findByEmail(param.getEmail())
                .orElseThrow(() -> new NotExistMemberDataException(("email = " + param.getEmail())));

        if (!member.checkPassword(param.getPassword())) {
            throw new InvalidPasswordException();
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotExistMemberDataException("email = " + email));
    }
}
