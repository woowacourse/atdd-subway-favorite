package wooteco.subway.service.member;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.common.AuthorizationType;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.AlreadyExistsEmailException;
import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.exception.LoginFailException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Transactional
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public MemberResponse createMember(MemberRequest request) {
		try {
			Member member = memberRepository.save(request.toMember());
			return MemberResponse.of(member);
		} catch (DbActionExecutionException e) {
			if (e.getCause() instanceof DuplicateKeyException) {
				throw new AlreadyExistsEmailException();
			}
			throw e;
		}
	}

	public void updateMember(Long id, UpdateMemberRequest request) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(id + "에 해당하는 회원이 없습니다."));
		Member updatedMember = member.makeMemberUpdateBy(request.getName(), request.getPassword());
		memberRepository.save(updatedMember);
	}

	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	public TokenResponse createJwtToken(LoginRequest request) {
		Member member = findMember(request.getEmail());
		if (!member.checkPassword(request.getPassword())) {
			throw new LoginFailException();
		}

		String token = jwtTokenProvider.createToken(request.getEmail());
		return new TokenResponse(token, AuthorizationType.BEARER.getPrefix());
	}

	private Member findMember(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("해당하는 이메일이 없습니다."));
	}

	public MemberResponse findMemberResponseByEmail(String email) {
		return MemberResponse.of(findMemberByEmail(email));
	}

	@Transactional(readOnly = true)
	public Member findMemberByEmail(String email) {
		return findMember(email);
	}

	public boolean loginWithForm(LoginRequest request) {
		Member member = findMemberByEmail(request.getEmail());
		return member.checkPassword(request.getPassword());
	}
}
