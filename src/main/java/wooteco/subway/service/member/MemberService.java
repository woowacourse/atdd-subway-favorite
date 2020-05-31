package wooteco.subway.service.member;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.member.exception.DuplicatedEmailException;
import wooteco.subway.service.member.exception.EmailNotFoundException;
import wooteco.subway.service.member.exception.MemberNotFoundException;
import wooteco.subway.service.member.exception.WrongPasswordException;

@Service
public class MemberService {
	private MemberRepository memberRepository;
	private JwtTokenProvider jwtTokenProvider;

	public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public Member createMember(Member member) {
		if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
			throw new DuplicatedEmailException();
		}

		return memberRepository.save(member);
	}

	public Member updateMemberByUser(Member member, UpdateMemberRequest param) {
		return updateMember(member, param);
	}

	public Member updateMemberByAdmin(Long id, UpdateMemberRequest updateMemberRequest) {
		Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
		return updateMember(member, updateMemberRequest);
	}

	private Member updateMember(Member member, UpdateMemberRequest param) {
		member.update(param.getName(), param.getPassword());
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

	public String createToken(LoginRequest param) {
		Member member = memberRepository.findByEmail(param.getEmail())
			.orElseThrow(EmailNotFoundException::new);
		if (!member.checkPassword(param.getPassword())) {
			throw new WrongPasswordException();
		}

		return jwtTokenProvider.createToken(param.getEmail());
	}

	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);
	}
}
