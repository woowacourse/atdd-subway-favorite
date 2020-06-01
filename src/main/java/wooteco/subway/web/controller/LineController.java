package wooteco.subway.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.line.Line;
import wooteco.subway.service.line.LineService;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineRequest;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

@RequestMapping("/lines")
@RestController
public class LineController {

	private final LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest view) {
		Line persistLine = lineService.save(view.toLine());

		return ResponseEntity
			.created(URI.create("/lines/" + persistLine.getId()))
			.body(LineResponse.of(persistLine));
	}

	@GetMapping
	public ResponseEntity<List<LineResponse>> showLine() {
		return ResponseEntity
			.ok()
			.body(LineResponse.listOf(lineService.findLines()));
	}

	@GetMapping("{id}")
	public ResponseEntity<LineDetailResponse> retrieveLine(@PathVariable Long id) {
		return ResponseEntity
			.ok()
			.body(lineService.retrieveLine(id));
	}

	@PutMapping("{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest view) {
		lineService.updateLine(id, view);
		return ResponseEntity
			.ok()
			.build();
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.deleteLineById(id);
		return ResponseEntity
			.noContent()
			.build();
	}

	@PostMapping("{id}/stations")
	public ResponseEntity<Void> addLineStation(@PathVariable Long id, @RequestBody LineStationCreateRequest view) {
		lineService.addLineStation(id, view);
		return ResponseEntity
			.ok()
			.build();
	}

	@DeleteMapping("/{lineId}/stations/{stationId}")
	public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @PathVariable Long stationId) {
		lineService.removeLineStation(lineId, stationId);
		return ResponseEntity
			.noContent()
			.build();
	}

	@GetMapping("/detail")
	public ResponseEntity<WholeSubwayResponse> wholeLines() {
		WholeSubwayResponse result = lineService.findLinesWithStations();
		return ResponseEntity
			.ok()
			.body(result);
	}

}
