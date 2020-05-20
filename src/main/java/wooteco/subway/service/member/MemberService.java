package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exceptions.DuplicatedEmailException;
import wooteco.subway.exceptions.InvalidEmailException;
import wooteco.subway.exceptions.InvalidPasswordException;
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
        String email = member.getEmail();
        if (memberRepository.findByEmail(email).isPresent()) {
	        throw new DuplicatedEmailException(email);
        }
	    return memberRepository.save(member);
    }

	public void updateMember(Long id, UpdateMemberRequest param) {
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		member.update(param.getName(), param.getPassword());
		memberRepository.save(member);
	}

	public void updateMember(Member member, UpdateMemberRequest param) {
		member.update(param.getName(), param.getPassword());
		memberRepository.save(member);
	}

	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	public void deleteMember(Member member) {
		memberRepository.delete(member);
	}

	public String createToken(LoginRequest param) {
		String email = param.getEmail();
		Member member = memberRepository.findByEmail(email).orElseThrow(() -> new InvalidEmailException(email));
		if (!member.checkPassword(param.getPassword())) {
			throw new InvalidPasswordException();
		}

		return jwtTokenProvider.createToken(email);
	}

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    public boolean loginWithForm(String email, String password) {
        Member member = findMemberByEmail(email);
        return member.checkPassword(password);
    }
}
