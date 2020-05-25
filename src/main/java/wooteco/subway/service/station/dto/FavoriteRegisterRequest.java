package wooteco.subway.service.station.dto;

public class FavoriteRegisterRequest {
    private String startStationName;
    private String endStationName;

    public FavoriteRegisterRequest() {
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }
}
