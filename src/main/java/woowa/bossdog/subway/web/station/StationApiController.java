package woowa.bossdog.subway.web.station;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import woowa.bossdog.subway.service.station.StationService;
import woowa.bossdog.subway.service.station.dto.StationRequest;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stations")
public class StationApiController {

    private final StationService stationService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody StationRequest request) {
        final StationResponse stationResponse = stationService.createStation(request);
        return ResponseEntity.created(URI.create("/stations/" + stationResponse.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> list() {
        return ResponseEntity.ok().body(stationService.listStations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> find(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(stationService.findStation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }

}
