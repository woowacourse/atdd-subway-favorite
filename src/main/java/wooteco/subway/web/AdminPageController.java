package wooteco.subway.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wooteco.subway.service.line.LineService;
import wooteco.subway.service.station.StationService;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
    private final LineService lineService;
    private final StationService stationService;

    public AdminPageController(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "admin/index";
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

    @GetMapping(value = "/stations", produces = MediaType.TEXT_HTML_VALUE)
    public String stationPage(Model model) {
        model.addAttribute("stations", stationService.findStations());
        return "admin/admin-station";
    }
}
