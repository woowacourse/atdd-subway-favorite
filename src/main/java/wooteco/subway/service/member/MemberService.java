package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.member.exception.DuplicateEmailException;
import wooteco.subway.service.member.exception.InvalidMemberEmailException;
import wooteco.subway.service.member.exception.InvalidMemberIdException;
import wooteco.subway.service.member.exception.InvalidMemberPasswordException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final FavoriteService favoriteService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository,
        FavoriteService favoriteService, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.favoriteService = favoriteService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }

        Member member = memberRequest.toMember();

        Member savedMember = memberRepository.save(member);
        return MemberResponse.of(savedMember);
    }

    @Transactional
    public void updateMember(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(InvalidMemberIdException::new);
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        favoriteService.deleteByMemberId(id);
        memberRepository.deleteById(id);
    }

    @Transactional
    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
            .orElseThrow(InvalidMemberEmailException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new InvalidMemberPasswordException();
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    @Transactional(readOnly = true)
    public MemberResponse findMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(InvalidMemberEmailException::new);
        return MemberResponse.of(member);
    }
}
