package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.service.path.PathService;
import wooteco.subway.service.path.dto.PathRequest;
import wooteco.subway.service.path.dto.PathResponse;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(PathRequest pathRequest) {
        return ResponseEntity.ok(pathService.findPath(pathRequest));
    }
}
