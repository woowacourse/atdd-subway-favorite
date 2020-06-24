package wooteco.subway.domain.station;

import javax.persistence.Entity;

import wooteco.subway.domain.BaseEntity;

@Entity
public class Station extends BaseEntity {
    private String name;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isSame(Long id) {
        return this.id.equals(id);
    }

    public String getName() {
        return name;
    }
}
