package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.LoginResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.web.member.argumentresolver.annotation.LoginMember;
import wooteco.subway.web.member.exception.InvalidPasswordException;

import javax.validation.Valid;

@RestController
public class LoginMemberController {
    private MemberService memberService;

    public LoginMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest param) throws InvalidPasswordException {
        String token = memberService.createToken(param);
        return ResponseEntity.ok().body(new LoginResponse(token, "bearer", param.getEmail()));
    }

    @GetMapping({"/me/basic", "/me/session", "/me/bearer"})
    public ResponseEntity<MemberResponse> getMemberOfMineBasic(@LoginMember Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }
}
