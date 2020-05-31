package wooteco.subway.service.member;

import static wooteco.subway.exception.InvalidMemberException.DUPLICATED_EMAIL;
import static wooteco.subway.exception.InvalidMemberException.FAIL_TO_CREATE;
import static wooteco.subway.exception.InvalidMemberException.NOT_FOUND_MEMBER;
import static wooteco.subway.exception.InvalidMemberException.WRONG_PASSWORD;

import javax.validation.Valid;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.InvalidMemberException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {
	private MemberRepository memberRepository;
	private JwtTokenProvider jwtTokenProvider;

	public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public MemberResponse createMember(@Valid MemberRequest request) {
		Member member = request.toMember();
		try {
			memberRepository.save(member);
		} catch (DbActionExecutionException e) {
			if (e.getCause() instanceof DuplicateKeyException) {
				throw new InvalidMemberException(DUPLICATED_EMAIL, member.getEmail());
			}
			throw new InvalidMemberException(FAIL_TO_CREATE + e.getMessage(), member.getEmail());
		}
		return MemberResponse.of(member);
	}

	public String createToken(LoginRequest request) {
		Member member = findMemberByEmail(request.getEmail());
		if (!member.checkPassword(request.getPassword())) {
			throw new InvalidMemberException(WRONG_PASSWORD, request.getEmail());
		}
		return jwtTokenProvider.createToken(request.getEmail());
	}

	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new InvalidMemberException(NOT_FOUND_MEMBER, email));
	}

	public void updateMember(Member member, UpdateMemberRequest param) {
		Member persistMember = findMemberByEmail(member.getEmail());
		persistMember.update(param.getName(), param.getPassword());
		memberRepository.save(persistMember);
	}

	public void deleteMember(Member member) {
		Member persistMember = findMemberByEmail(member.getEmail());
		deleteMember(persistMember.getId());
	}

	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}
}
