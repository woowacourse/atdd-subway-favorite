package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.LoginFailException;
import wooteco.subway.exception.MemberNotFoundException;
import wooteco.subway.infra.TokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Transactional
@Service
public class MemberService {
    private static final String TOKEN_TYPE = "bearer";

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public MemberResponse createMember(MemberRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, UpdateMemberRequest request) {
        Member member = memberRepository.findById(id)
            .orElseThrow(MemberNotFoundException::new);
        member.update(request.getName(), request.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        if (!memberRepository.findById(id).isPresent()) {
            throw new MemberNotFoundException();
        }
        memberRepository.deleteById(id);
    }

    public TokenResponse createJwtToken(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(LoginFailException::new);
        if (!member.checkPassword(request.getPassword())) {
            throw new LoginFailException();
        }

        String token = tokenProvider.createToken(request.getEmail());
        return new TokenResponse(token, TOKEN_TYPE);
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    public MemberResponse findMemberById(long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }
}
