package wooteco.subway.domain.member;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import wooteco.subway.domain.station.Station;

public class Favorite {
    @Id
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {
    }

    public Favorite(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Favorite(Long id, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }
}
