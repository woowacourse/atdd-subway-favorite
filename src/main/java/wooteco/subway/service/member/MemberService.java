package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exceptions.DuplicatedEmailException;
import wooteco.subway.exceptions.InvalidEmailException;
import wooteco.subway.exceptions.InvalidPasswordException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
@Transactional
public class MemberService {
	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public Member createMember(Member member) {
		String email = member.getEmail();
		if (memberRepository.existsByEmail(email)) {
			throw new DuplicatedEmailException(email);
		}
		return memberRepository.save(member);
	}

	public void updateMember(Member member, UpdateMemberRequest param) {
		member.update(param.getName(), param.getPassword());
		memberRepository.save(member);
	}

	public void deleteMember(Member member) {
		memberRepository.delete(member);
	}

	public String createToken(LoginRequest param) {
		String email = param.getEmail();
		Member member = findMemberByEmail(email);
		if (member.hasIdenticalPasswordWith(param.getPassword())) {
			return jwtTokenProvider.createToken(email);
		}
		throw new InvalidPasswordException();
	}

    public Member findMemberByEmail(String email) {
	    return memberRepository.findByEmail(email).orElseThrow(() -> new InvalidEmailException(email));
    }
}
