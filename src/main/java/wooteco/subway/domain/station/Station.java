package wooteco.subway.domain.station;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@AttributeOverride(name = "id", column = @Column(name = "STATION_ID"))
public class Station extends BaseEntity {

    @Column(name = "STATION_NAME", unique = true)
    private String name;

    public Station(long id, String name) {
        super.id = id;
        this.name = name;
    }

    public static Station of(String name) {
        return new Station(name);
    }

    public boolean isSameId(Long stationId) {
        return super.id.equals(stationId);
    }
}
