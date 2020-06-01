package wooteco.subway.web.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.line.Line;
import wooteco.subway.service.line.LineService;
import wooteco.subway.service.line.dto.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(
            @Valid @RequestBody LineRequest request
    ) {
        Line persistLine = lineService.save(request.toLine());

        return ResponseEntity
                .created(URI.create("/lines/" + persistLine.getId()))
                .body(LineResponse.from(persistLine));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLine() {
        return ResponseEntity.ok().body(LineResponse.listFrom(lineService.findLines()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineDetailResponse> retrieveLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.getLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(
            @PathVariable Long id,
            @Valid @RequestBody LineRequest request
    ) {
        lineService.updateLine(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/stations")
    public ResponseEntity<Void> addLineStation(
            @PathVariable Long id,
            @Valid @RequestBody LineStationCreateRequest view
    ) {
        lineService.addLineStation(id, view);
        return ResponseEntity.created(URI.create("/lines/" + id + "/stations")).build();
    }

    @DeleteMapping("/{id}/stations/{stationId}")
    public ResponseEntity<Void> removeLineStation(
            @PathVariable Long id,
            @PathVariable Long stationId
    ) {
        lineService.removeLineStation(id, stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail")
    public ResponseEntity<WholeSubwayResponse> wholeLines() {
        WholeSubwayResponse result = lineService.findLinesWithStations();
        return ResponseEntity.ok().body(result);
    }
}
