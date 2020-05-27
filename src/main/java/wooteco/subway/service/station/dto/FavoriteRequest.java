package wooteco.subway.service.station.dto;

public class FavoriteRequest {
    private String startStationName;
    private String endStationName;

    public FavoriteRequest() {
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }
}
