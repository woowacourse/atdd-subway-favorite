package wooteco.subway.domain.line;

import java.time.LocalDateTime;
import java.util.Objects;

import wooteco.subway.web.controlleradvice.exception.LineStationException;

public class LineStation {
    private static final int POSITIVE_NUMBER_THRESHOLD = 0;
    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LineStation(Long preStationId, Long stationId, int distance, int duration,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateLineStation(preStationId, stationId, distance, duration);
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static LineStation of(Long preStationId, Long stationId, int distance, int duration) {
        return new LineStation(preStationId, stationId, distance, duration, LocalDateTime.now(),
            LocalDateTime.now());
    }

    public void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isFirstLineStation() {
        return Objects.isNull(preStationId);
    }

    public boolean hasSameStations(LineStation lineStation) {
        return (Objects.equals(this.stationId, lineStation.stationId) && Objects.equals(
            this.preStationId, lineStation.preStationId))
            || (Objects.equals(this.preStationId, lineStation.stationId));
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private void validateLineStation(Long preStationId, Long stationsId, int distance,
        int duration) {
        validateStation(stationsId);
        validateSameId(preStationId, stationsId);
        validateOverZero(distance, duration);
    }

    private void validateStation(Long stationsId) {
        if (Objects.isNull(stationsId)) {
            throw new IllegalArgumentException("현재역은 비어있을 수 없습니다.");
        }
    }

    private void validateSameId(Long preStationId, Long stationsId) {
        if (stationsId.equals(preStationId)) {
            throw new LineStationException("같은 역을 출발지점과 도착지점으로 정할 수 없습니다.");
        }
    }

    private void validateOverZero(int distance, int duration) {
        if (distance < POSITIVE_NUMBER_THRESHOLD || duration < POSITIVE_NUMBER_THRESHOLD) {
            throw new LineStationException("거리나 시간은 음수일 수 없습니다.");
        }
    }
}
