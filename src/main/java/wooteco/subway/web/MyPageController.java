package wooteco.subway.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    @GetMapping(value = "/mypage", produces = MediaType.TEXT_HTML_VALUE)
    public String myPage() {
        return "service/mypage";
    }

    @GetMapping(value = "/mypage-edit", produces = MediaType.TEXT_HTML_VALUE)
    public String myPageEdit() {
        return "service/mypage-edit";
    }

    @GetMapping(value = "/favorites", produces = MediaType.TEXT_HTML_VALUE)
    public String favoritesPage() {
        return "service/favorite";
    }
}
