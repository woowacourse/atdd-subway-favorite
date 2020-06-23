package woowa.bossdog.subway.web.path;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import woowa.bossdog.subway.service.path.PathService;
import woowa.bossdog.subway.service.path.dto.PathRequest;
import woowa.bossdog.subway.service.path.dto.PathResponse;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class PathApiController {

    private final PathService pathService;

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> search(@Valid PathRequest request) {
        final PathResponse response = pathService.findPath(request);
        return ResponseEntity.ok().body(response);
    }
}
