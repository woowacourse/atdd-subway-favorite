package wooteco.subway.web.member;

import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.member.MemberService;

@RestController
public class FavoriteController {
    private MemberService memberService;

    public FavoriteController(MemberService memberService) {
        this.memberService = memberService;
    }

}
