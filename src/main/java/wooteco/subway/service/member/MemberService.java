package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public Member createMember(Member member) {
		return memberRepository.save(member);
	}

	@Transactional
	public void updateMember(Member member, UpdateMemberRequest param) {
		Member updated = member.updateNameAndPassword(param.getName(),
			param.getPassword());
		memberRepository.save(updated);
	}

	@Transactional
	public void deleteMember(Member member) {
		memberRepository.delete(member);
	}

	@Transactional
	public String createToken(LoginRequest param) {
		Member member = findMemberByEmail(param.getEmail());
		checkPassword(param, member);

		return jwtTokenProvider.createToken(param.getEmail());
	}

	private void checkPassword(LoginRequest param, Member member) {
		if (!member.checkPassword(param.getPassword())) {
			throw new IllegalArgumentException("잘못된 패스워드입니다.");
		}
	}

	@Transactional
	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new InvalidAuthenticationException(
				email + "은 존재하지 않는 회원입니다."));
	}
}
