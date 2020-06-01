package wooteco.subway.service.member;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.IllegalLoginRequestException;
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

	public Member createMember(Member member) {
		return memberRepository.save(member);
	}

	public Member updateMemberByUser(Member member, UpdateMemberRequest request) {
		return updateMember(member, request);
	}

	public Member updateMemberByAdmin(Long id, UpdateMemberRequest request) {
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		return updateMember(member, request);
	}

	private Member updateMember(Member member, UpdateMemberRequest request) {
		member.update(request.getName(), request.getPassword());
		return memberRepository.save(member);
	}

	public void deleteMemberByUser(Member member) {
		deleteMember(member.getId());
	}

	public void deleteMemberByAdmin(Long id) {
		deleteMember(id);
	}

	private void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	public String createToken(LoginRequest request) {
		Member member = findMemberByEmail(request.getEmail());
		if (!member.checkPassword(request.getPassword())) {
			throw new IllegalLoginRequestException("패스워드가 일치하지 않습니다.");
		}

		return jwtTokenProvider.createToken(request.getEmail());
	}

	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalLoginRequestException("해당 이메일이 존재하지 않습니다."));
	}
}
