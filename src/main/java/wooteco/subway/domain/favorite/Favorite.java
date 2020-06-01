package wooteco.subway.domain.favorite;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class Favorite {
    @Id
    Long id;
    @Column("pre_station")
    @NotNull
    Long preStationId;
    @Column("station")
    @NotNull
    Long stationId;

    public Favorite() {
    }

    public Favorite(Long preStationId, Long stationId) {
        this(null, preStationId, stationId);
    }

    public Favorite(Long id, Long preStationId, Long stationId) {
        Objects.requireNonNull(preStationId, "출발역이 null일 수 없습니다.");
        Objects.requireNonNull(stationId, "도착역이 null일 수 없습니다.");

        if (Objects.deepEquals(preStationId, stationId)) {
            throw new IllegalArgumentException("도착역과 출발역은 같을 수 없습니다. station - " + preStationId);
        }

        this.id = id;
        this.preStationId = preStationId;
        this.stationId = stationId;
    }

    public Long getId() {
        return id;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public boolean isSameId(Long favoriteId) {
        return this.id.equals(favoriteId);
    }
}
