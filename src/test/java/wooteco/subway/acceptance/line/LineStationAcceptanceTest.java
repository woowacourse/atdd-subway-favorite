package wooteco.subway.acceptance.line;

import wooteco.subway.acceptance.AcceptanceTest;

public class LineStationAcceptanceTest extends AcceptanceTest {

    // @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    // @Test
    // void manageLineStation() {
    //     StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
    //     StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
    //     StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
    //
    //     LineResponse lineResponse = createLine("2호선");
    //
    //     addLineStation(lineResponse.getId(), null, stationResponse1.getId());
    //     addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
    //     addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());
    //
    //     LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
    //     assertThat(lineDetailResponse.getStations()).hasSize(3);
    //
    //     removeLineStation(lineResponse.getId(), stationResponse2.getId());
    //
    //     LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineResponse.getId());
    //     assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    // }
}
