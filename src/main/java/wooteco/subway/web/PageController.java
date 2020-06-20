package wooteco.subway.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import wooteco.subway.config.TokenConfig;
import wooteco.subway.service.line.LineService;
import wooteco.subway.service.station.StationService;

@Controller
public class PageController {
	private final LineService lineService;
	private final StationService stationService;
	private final TokenConfig tokenConfig;

	public PageController(LineService lineService, StationService stationService,
		TokenConfig tokenConfig) {
		this.lineService = lineService;
		this.stationService = stationService;
		this.tokenConfig = tokenConfig;
	}

	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String index() {
		return "admin/index";
	}

	@GetMapping(value = "/stations", produces = MediaType.TEXT_HTML_VALUE)
	public String stationPage(Model model) {
		model.addAttribute("stations", stationService.findStations());
		return "admin/admin-station";
	}

	@GetMapping(value = "/lines", produces = MediaType.TEXT_HTML_VALUE)
	public String linePage(Model model) {
		model.addAttribute("lines", lineService.findLines());
		return "admin/admin-line";
	}

	@GetMapping(value = "/edges", produces = MediaType.TEXT_HTML_VALUE)
	public String edgePage() {
		return "admin/admin-edge";
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

	@GetMapping(value = "/mypage", produces = MediaType.TEXT_HTML_VALUE)
	public String myPage() {
		return "service/mypage-edit";
	}

	@GetMapping(value = "/favorites", produces = MediaType.TEXT_HTML_VALUE)
	public String favoritesPage() {
		return "service/favorite";
	}
}
