package wooteco.subway.service.station.dto;

public class FavoritePathRequest {
    private String startStationName;
    private String endStationName;

    public FavoritePathRequest() {
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }
}
