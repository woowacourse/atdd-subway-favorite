package wooteco.subway.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *    class description
 *
 *    @author HyungJu An
 */
@Controller
public class ServicePageController {
	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String index() {
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

	@GetMapping(value = "/logout", produces = MediaType.TEXT_HTML_VALUE)
	public String logout() {
		return "service/index";
	}

	@GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
	public String loginPage() {
		return "service/login";
	}

	@GetMapping(value = "/me/favorites", produces = MediaType.TEXT_HTML_VALUE)
	public String favoritesPage() {
		return "service/favorite";
	}

	@GetMapping(value = "/mypage", produces = MediaType.TEXT_HTML_VALUE)
	public String myPage() {
		return "service/mypage";
	}

	@GetMapping(value = "/mypage-edit", produces = MediaType.TEXT_HTML_VALUE)
	public String myPageEdit() {
		return "service/mypage-edit";
	}
}
