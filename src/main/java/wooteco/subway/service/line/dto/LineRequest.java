package wooteco.subway.service.line.dto;

import wooteco.subway.domain.line.Line;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

public class LineRequest {
    @NotEmpty
    private String name;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private int intervalTime;

    public LineRequest() {
    }

    public LineRequest(final String name, final LocalTime startTime, final LocalTime endTime, final int intervalTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
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

    public Line toLine() {
        return new Line(name, startTime, endTime, intervalTime);
    }
}
