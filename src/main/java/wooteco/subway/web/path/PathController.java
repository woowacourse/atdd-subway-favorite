package wooteco.subway.web.path;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.path.PathService;
import wooteco.subway.service.path.dto.PathRequest;
import wooteco.subway.service.path.dto.PathResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(
            @Valid PathRequest pathRequest
    ) {
        PathResponse response = pathService.findPath(
                pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getType());
        return ResponseEntity.ok().body(response);
    }
}
