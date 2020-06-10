package wooteco.subway.service.line.dto;

import wooteco.subway.domain.line.Line;

import javax.validation.constraints.NotEmpty;
import java.time.LocalTime;

public class LineRequest {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    private LineRequest() {
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
