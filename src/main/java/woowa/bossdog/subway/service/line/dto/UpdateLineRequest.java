package woowa.bossdog.subway.service.line.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateLineRequest {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    public UpdateLineRequest(final String name, final LocalTime startTime, final LocalTime endTime, final int intervalTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }
}
