package wooteco.subway.service.member;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {
	private MemberRepository memberRepository;
	private FavoriteService favoriteService;
	private JwtTokenProvider jwtTokenProvider;

	public MemberService(MemberRepository memberRepository,
		FavoriteService favoriteService, JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.favoriteService = favoriteService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public Member createMember(Member member) {
		try {
			return memberRepository.save(member);
		} catch (DbActionExecutionException e) {
			if (e.getCause() instanceof DuplicateKeyException) {
				throw new DuplicatedEmailException(member.getEmail());
			}
			throw e;
		}
	}

	public void updateMember(Long id, UpdateMemberRequest param) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new MemberNotFoundException(String.valueOf(id)));
		member.update(param.getName(), param.getPassword());
		memberRepository.save(member);
	}

	@Transactional(rollbackFor = DbActionExecutionException.class)
	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
		favoriteService.deleteFavorites(id);
	}

	public String createToken(LoginRequest param) {
		Member member = memberRepository.findByEmail(param.getEmail())
			.orElseThrow(() -> new NonExistEmailException(param.getEmail()));
		if (!member.checkPassword(param.getPassword())) {
			throw new WrongPasswordException();
		}

		return jwtTokenProvider.createToken(param.getEmail());
	}

	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberNotFoundException(email));
	}
}
