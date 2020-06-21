package woowa.bossdog.subway.web.line;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import woowa.bossdog.subway.service.line.LineService;
import woowa.bossdog.subway.service.line.dto.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines")
public class LineApiController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody LineRequest request) {
        final LineResponse lineResponse = lineService.createLine(request);
        return ResponseEntity
                .created(URI.create("/lines/" + lineResponse.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> list() {
        return ResponseEntity.ok().body(lineService.listLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> find(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") Long id,
            @RequestBody UpdateLineRequest updateRequest
    ) {
        lineService.updateLine(id, updateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/stations")
    public ResponseEntity<Void> addLineStation(
            @PathVariable("id") Long id,
            @RequestBody LineStationRequest request
    ) {
        lineService.addLineStation(id, request);
        return ResponseEntity.created(URI.create("/lines/" + id + "/stations/" + request.getStationId())).build();
    }

    @DeleteMapping("/{id}/stations/{stationId}")
    public ResponseEntity<Void> deleteLineStation(
            @PathVariable("id") Long id,
            @PathVariable("stationId") Long stationId
    ) {
        lineService.deleteLineStation(id, stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stations")
    public ResponseEntity<LineDetailResponse> findLineDetail(
        @PathVariable("id") Long id
    ) {
        final LineDetailResponse response = lineService.findLineDetail(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/detail")
    public ResponseEntity<WholeSubwayResponse> listLineDetail() {
        WholeSubwayResponse response = lineService.listLineDetail();
        return ResponseEntity.ok().body(response);
    }
}
