package wooteco.subway.web.member;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import wooteco.subway.service.member.MemberService;

@Controller
public class MemberPageController {
    private MemberService memberService;

    public MemberPageController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = "/join", produces = MediaType.TEXT_HTML_VALUE)
    public String joinPage() {
        return "service/join";
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String loginPage() {
        return "service/login";
    }

    @GetMapping(value = "/mypage", produces = MediaType.TEXT_HTML_VALUE)
    public String myPage() {
        return "service/mypage-edit";
    }


}
