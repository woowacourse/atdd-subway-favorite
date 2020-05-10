package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.service.PathService;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity findPath(@RequestParam String source,
                                   @RequestParam String target,
                                   @RequestParam PathType type) {
        return ResponseEntity.ok(pathService.findPath(source, target, type));
    }
}
