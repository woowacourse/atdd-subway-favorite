package woowa.bossdog.subway.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "admin/index";
    }

    @GetMapping(value = "/stations", produces = MediaType.TEXT_HTML_VALUE)
    public String stationPage() {
        return "admin/admin-station";
    }

    @GetMapping(value = "/lines", produces = MediaType.TEXT_HTML_VALUE)
    public String linePage() {
        return "admin/admin-line";
    }

    @GetMapping(value = "/edges", produces = MediaType.TEXT_HTML_VALUE)
    public String lineStationPage() {
        return "admin/admin-edge";
    }

    @GetMapping(value = "/service", produces = MediaType.TEXT_HTML_VALUE)
    public String servicePage() {
        return "service/index";
    }

    @GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE)
    public String searchPage() {
        return "service/search";
    }

    @GetMapping(value = "/map", produces = MediaType.TEXT_HTML_VALUE)
    public String mapPage() {
        return "service/map";
    }

    @GetMapping(value = "/join", produces = MediaType.TEXT_HTML_VALUE)
    public String join() {
        return "service/join";
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {
        return "service/login";
    }

    @GetMapping(value = "/mypage", produces = MediaType.TEXT_HTML_VALUE)
    public String myPage() {
        return "service/mypage";
    }

    @GetMapping(value = "/mypage-edit", produces = MediaType.TEXT_HTML_VALUE)
    public String myPageEdit() {
        return "service/mypage-edit";
    }

    @GetMapping(value = "/myfavorites", produces = MediaType.TEXT_HTML_VALUE)
    public String favorites() {
        return "service/favorite";
    }
}
