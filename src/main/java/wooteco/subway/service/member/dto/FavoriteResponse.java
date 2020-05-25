package wooteco.subway.service.member.dto;

public class FavoriteResponse {
    private Long departStationId;
    private String departStationName;
    private Long arriveStationId;
    private String arriveStationName;

    public FavoriteResponse(Long departStationId, String departStationName, Long arriveStationId,
            String arriveStationName) {
        this.departStationId = departStationId;
        this.departStationName = departStationName;
        this.arriveStationId = arriveStationId;
        this.arriveStationName = arriveStationName;
    }

    // public static List<FavoriteResponse> listOf(List<Favorite> favorites, Map<Long, Station> stationMap) {
    //     return favorites.stream()
    //             .map(favorite -> new FavoriteResponse(stationMap.get(favorite.getDepartId()), favorite.getArriveId()))
    //             .collect(Collectors.toList());
    // }

    // public static List<FavoriteResponse> listOf(Member member) {
    //
    // }

    public Long getDepartStationId() {
        return departStationId;
    }

    public String getDepartStationName() {
        return departStationName;
    }

    public Long getArriveStationId() {
        return arriveStationId;
    }

    public String getArriveStationName() {
        return arriveStationName;
    }
}
