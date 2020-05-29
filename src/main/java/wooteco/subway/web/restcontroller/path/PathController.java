package wooteco.subway.web.restcontroller.path;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.path.PathType;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.path.PathService;
import wooteco.subway.service.path.dto.PathRequest;
import wooteco.subway.service.path.dto.PathResponse;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(PathRequest pathRequest) {
        return ResponseEntity.ok(pathService.findPath(pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getType()));
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity<Void> addLineStation(@PathVariable Long lineId,
        @RequestBody LineStationCreateRequest view) {
        pathService.addLineStation(lineId, view);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/stations/{stationId}")
    public ResponseEntity removeLineStation(@PathVariable Long lineId,
        @PathVariable Long stationId) {
        pathService.removeLineStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
