package wooteco.subway.domain.station;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.Id;

import wooteco.subway.jdbc.BaseEntity;

public class Station extends BaseEntity {
    @Id
    private Long id;
    private String name;

    public Station() {
        this(null, null);
    }

    public Station(String name) {
        this(null, name);
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(Long id, String name, LocalDateTime createdAt) {
        super(createdAt, null);
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Station station = (Station)o;
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
