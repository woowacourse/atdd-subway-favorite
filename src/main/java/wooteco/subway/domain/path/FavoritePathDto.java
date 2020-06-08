package wooteco.subway.domain.path;

import wooteco.subway.domain.station.Station;

public class FavoritePathDto {
    private Long id;
    private Station source;
    private Station target;

    public FavoritePathDto(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
