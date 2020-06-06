package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Transactional
	public Member createMember(Member member) {
		return memberRepository.save(member);
	}

	@Transactional(readOnly = true)
	public String createToken(LoginRequest param) {
		Member member = memberRepository.findByEmail(param.getEmail())
		                                .orElseThrow(() -> new InvalidMemberException("email이 동일한 member가 존재하지 않습니다."));

		if (!member.checkPassword(param.getPassword())) {
			throw new InvalidMemberException("패스워드가 일치하지 않습니다.");
		}

		return jwtTokenProvider.createToken(param.getEmail());
	}

	@Transactional(readOnly = true)
	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
		                       .orElseThrow(() -> new InvalidMemberException("email이 동일한 member가 존재하지 않습니다."));
	}

	@Transactional
	public void updateMember(Long id, UpdateMemberRequest param) {
		Member member = memberRepository.findById(id)
		                                .orElseThrow(() -> new InvalidMemberException("id에 해당하는 member가 존재하지 않습니다."));
		updateMember(member, param);
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
	public void deleteMember(Member member) {
		deleteMember(member.getId());
	}

}
