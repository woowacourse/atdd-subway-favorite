package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.path.PathType;
import wooteco.subway.service.path.PathService;

@RequestMapping("/paths")
@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity findPath(@RequestParam String source,
        @RequestParam String target,
        @RequestParam PathType type) {
        return ResponseEntity.ok(pathService.findPath(source, target, type));
    }
}
