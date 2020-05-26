package wooteco.subway.service.member;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.station.NotFoundStationException;

@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final StationRepository stationRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public MemberService(final MemberRepository memberRepository,
		final StationRepository stationRepository,
		final JwtTokenProvider jwtTokenProvider) {
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public Member createMember(final Member member) {
		memberRepository.findByEmail(member.getEmail()).ifPresent(m -> {
			throw new DuplicateEmailException(m.getEmail() + "는 이미 가입된 이메일입니다");
		});
		return memberRepository.save(member);
	}

	public void updateMember(final Long id, final UpdateMemberRequest param) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundMemberException("해당하는 아이디를 찾을 수 없습니다. : " + id));
		memberRepository.save(member.update(param.getName(), param.getPassword()));
	}

	public void deleteMember(final Long id) {
		memberRepository.deleteById(id);
	}

	public String createToken(final LoginRequest param) {
		Member member = memberRepository.findByEmail(param.getEmail())
			.orElseThrow(() -> new NotFoundMemberException("해당하는 이메일을 찾을 수 없습니다. : " + param.getEmail()));
		if (!member.checkPassword(param.getPassword())) {
			throw new IllegalPasswordException("잘못된 패스워드");
		}

		return jwtTokenProvider.createToken(param.getEmail());
	}

	public Member findMemberByEmail(final String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundMemberException("해당하는 이메일을 찾을 수 없습니다. : " + email));
	}

	public void createFavorite(final Member member, final FavoriteRequest favoriteRequest) {
		stationRepository.findById(favoriteRequest.getSourceId())
			.orElseThrow(() -> new NotFoundStationException("출발역을 찾을 수 없습니다. : " + favoriteRequest.getSourceId()));
		stationRepository.findById(favoriteRequest.getTargetId())
			.orElseThrow(() -> new NotFoundStationException("도착역을 찾을 수 없습니다. : " + favoriteRequest.getTargetId()));
		memberRepository.save(member.addFavorite(favoriteRequest.toFavorite()));
	}
}
