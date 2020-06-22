package wooteco.subway.service.station.dto;

public class FavoritePathResponse {
    private String startStationName;
    private String endStationName;

    public FavoritePathResponse() {
    }

    public FavoritePathResponse(String startStationName, String endStationName) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }
}
