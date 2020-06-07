package wooteco.subway.service.member;

import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.NoSuchMemberException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {
    public static final String WRONG_PASSWORD_ERROR_MESSAGE = "잘못된 패스워드";
    public static final String NO_EXIST_USER_ERROR_MESSAGE = "존재하지 않는 유저입니다.";
    public static final String NO_EXIST_ID_ERROR_MESSAGE = "존재하지 않는 Id입니다.";
    private static final String DB_SAVE_ERROR_MESSAGE = "사용자 저장에 실패하였습니다.";
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member createMember(Member member) {
        try {
            return memberRepository.save(member);
        } catch (DbActionExecutionException e) {
            throw new DuplicateEmailException(DB_SAVE_ERROR_MESSAGE, e);
        }
    }

    public void updateMember(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!member.getPassword().equals(param.getPassword())) {
            throw new IllegalArgumentException();
        }
        member.update(param.getName(), param.getNewPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
                .orElseThrow(RuntimeException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new RuntimeException(WRONG_PASSWORD_ERROR_MESSAGE);
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchMemberException(NO_EXIST_USER_ERROR_MESSAGE));
    }

    public boolean isExistMember(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    public boolean loginWithForm(String email, String password) {
        Member member = findMemberByEmail(email);
        return member.checkPassword(password);
    }

    public Member findById(Long id) {
        final Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_ID_ERROR_MESSAGE));
        return findMember;
    }
}
