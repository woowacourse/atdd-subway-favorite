package woowa.bossdog.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Station extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    private List<LineStation> lineStations = new ArrayList<>();

    @OneToMany(mappedBy = "preStation", cascade = CascadeType.ALL)
    private List<LineStation> preLineStations = new ArrayList<>();

    public Station(final String name) {
        this.name = name;
    }

    public Station(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Station station = (Station) o;
        return Objects.equals(getId(), station.getId()) &&
                Objects.equals(getName(), station.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
