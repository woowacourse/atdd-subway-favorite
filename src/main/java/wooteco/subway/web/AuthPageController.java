package wooteco.subway.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    @GetMapping(value = "/join", produces = MediaType.TEXT_HTML_VALUE)
    public String joinPage() {
        return "service/join";
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String loginPage() {
        return "service/login";
    }

    @GetMapping(value = "/logout", produces = MediaType.TEXT_HTML_VALUE)
    public String logoutPage() {
        return "service/logout";
    }
}
