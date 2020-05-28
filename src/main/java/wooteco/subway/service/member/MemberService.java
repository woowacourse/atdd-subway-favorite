package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.exception.LoginFailException;
import wooteco.subway.infra.TokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public MemberResponse createMember(MemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, UpdateMemberRequest request) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id + "에 해당하는 회원이 없습니다."));
        member.update(request.getName(), request.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new EntityNotFoundException(id + "에 해당하는 회원이 없습니다.");
        }
        memberRepository.deleteById(id);
    }

    public TokenResponse createJwtToken(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(LoginFailException::new);
        if (!member.checkPassword(request.getPassword())) {
            throw new LoginFailException();
        }

        String token = tokenProvider.createToken(request.getEmail());
        return new TokenResponse(token, "bearer");
    }

    public MemberResponse findMemberResponseByEmail(String email) {
        return MemberResponse.of(findMemberByEmail(email));
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("해당하는 이메일이 없습니다."));
    }

    public boolean loginWithForm(LoginRequest request) {
        Member member = findMemberByEmail(request.getEmail());
        return member.checkPassword(request.getPassword());
    }
}
