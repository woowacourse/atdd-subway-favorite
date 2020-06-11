package wooteco.subway.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wooteco.subway.service.line.LineService;
import wooteco.subway.service.station.StationService;

@Controller
public class ServicePageController {
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String indexPage() {
        return "service/index";
    }

    @GetMapping(value = "/map", produces = MediaType.TEXT_HTML_VALUE)
    public String mapPage() {
        return "service/map";
    }

    @GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE)
    public String searchPage() {
        return "service/search";
    }

    @GetMapping(value = "/join", produces = MediaType.TEXT_HTML_VALUE)
    public String joinPage() {
        return "service/join";
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String loginPage() {
        return "service/login";
    }

    @GetMapping(value = "/favorites", produces = MediaType.TEXT_HTML_VALUE)
    public String favoritesPage() {
        return "service/favorite";
    }

    @GetMapping(value = "/mypage", produces = MediaType.TEXT_HTML_VALUE)
    public String myPage() {
        return "service/mypage";
    }

    @GetMapping(value = "/mypage-edit", produces = MediaType.TEXT_HTML_VALUE)
    public String editMyPage() {
        return "service/mypage-edit";
    }
}
