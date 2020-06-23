package woowa.bossdog.subway.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowa.bossdog.subway.domain.Member;
import woowa.bossdog.subway.infra.JwtTokenProvider;
import woowa.bossdog.subway.repository.MemberRepository;
import woowa.bossdog.subway.service.member.dto.LoginRequest;
import woowa.bossdog.subway.service.member.dto.MemberRequest;
import woowa.bossdog.subway.service.member.dto.MemberResponse;
import woowa.bossdog.subway.service.member.dto.UpdateMemberRequest;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberResponse createMember(final MemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new ExistedEmailException();
        }
        final Member member = memberRepository.save(request.toMember());
        return MemberResponse.from(member);
    }

    public List<MemberResponse> listMembers() {
        return MemberResponse.listFrom(memberRepository.findAll());
    }

    public MemberResponse findMember(final Long id) {
        final Member member = memberRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        return MemberResponse.from(member);
    }

    @Transactional
    public void updateMember(final Long id, final UpdateMemberRequest request) {
        final Member member = memberRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        member.update(request);
    }

    @Transactional
    public void deleteMember(final Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(NotExistedEmailException::new);
    }

    public String createToken(final LoginRequest request) {
        Member member = findMemberByEmail(request.getEmail());
        if (!member.checkPassword(request.getPassword())) {
            throw new WrongPasswordException();
        }
        return jwtTokenProvider.createToken(request.getEmail());
    }
}
