package wooteco.subway.service.line.dto;

import wooteco.subway.domain.line.Line;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private String backgroundColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public LineResponse() {
    }

    public LineResponse(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime,
        String backgroundColor, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.backgroundColor = backgroundColor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getStartTime(), line.getEndTime(), line.getIntervalTime(),  line.getBackgroundColor(), line.getCreatedAt(), line.getUpdatedAt());
    }

    public static List<LineResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
}
