package wooteco.subway.service.member;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.member.InvalidAuthenticationException;

@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public MemberService(MemberRepository memberRepository,
		JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public Member createMember(Member member) {
		return memberRepository.save(member);
	}

	public void updateMember(Member member, UpdateMemberRequest param) {
		Member updated = member.updateNameAndPassword(param.getName(),
			param.getPassword());
		memberRepository.save(updated);
	}

	public void deleteMember(Member member) {
		memberRepository.delete(member);
	}

	public String createToken(LoginRequest param) {
		Member member = findMemberByEmail(param.getEmail());
		if (!member.checkPassword(param.getPassword())) {
			throw new RuntimeException("잘못된 패스워드입니다.");
		}

		return jwtTokenProvider.createToken(param.getEmail());
	}

	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new InvalidAuthenticationException(
				email + "은 존재하지 않는 회원입니다."));
	}
}
