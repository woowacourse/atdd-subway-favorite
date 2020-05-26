package wooteco.subway.service.line.dto;

import java.time.LocalTime;

import wooteco.subway.domain.line.Line;

public class LineRequest {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    private LineRequest() {
    }

    public Line toLine() {
        return new Line(name, startTime, endTime, intervalTime);
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
}
