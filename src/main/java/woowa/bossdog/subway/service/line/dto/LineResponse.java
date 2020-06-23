package woowa.bossdog.subway.service.line.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import woowa.bossdog.subway.domain.Line;

import java.time.LocalTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineResponse {

    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;


    private LineResponse(final Long id, final String name, final LocalTime startTime, final LocalTime endTime, final int intervalTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public static LineResponse from(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getStartTime(), line.getEndTime(), line.getIntervalTime());
    }

}
