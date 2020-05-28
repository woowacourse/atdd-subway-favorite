package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;

public class Favorite {
    @Id
    Long id;
    Long preStation;
    Long station;

    public Favorite() {
    }

    public Favorite(Long preStation, Long station) {
        this.preStation = preStation;
        this.station = station;
    }

    public Favorite(Long id, Long preStation, Long station) {
        this.id = id;
        this.preStation = preStation;
        this.station = station;
    }

	public boolean isSameId(Long favoriteId) {
		return this.id.equals(favoriteId);
	}

	public boolean hasSameStationsAs(Favorite favorite) {
		return this.preStation.equals(favorite.preStation) && this.station.equals(favorite.station);
	}

    public Long getId() {
        return id;
    }

    public Long getPreStation() {
        return preStation;
    }

    public Long getStation() {
        return station;
    }
}
