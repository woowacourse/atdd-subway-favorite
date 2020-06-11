package wooteco.subway.service.member.dto;

import java.util.List;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.station.Station;

public class FavoriteResponse {
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;
    private String sourceStationName;
    private String targetStationName;

    private FavoriteResponse() {
    }

    public FavoriteResponse(Favorite favorite, Station source, Station target) {
        this.id = favorite.getId();
        this.sourceStationId = source.getId();
        this.targetStationId = target.getId();
        this.sourceStationName = source.getName();
        this.targetStationName = target.getName();
    }

    public static FavoriteResponse of(final Favorite favorite, final List<Station> stations) {
        Station source = stations.stream()
            .filter(station -> station.getId().equals(favorite.getSourceStationId()))
            .findAny().orElseThrow(IllegalArgumentException::new);
        Station target = stations.stream()
            .filter(station -> station.getId().equals(favorite.getTargetStationId()))
            .findAny().orElseThrow(IllegalArgumentException::new);
        return new FavoriteResponse(favorite, source, target);
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public String getTargetStationName() {
        return targetStationName;
    }
}
