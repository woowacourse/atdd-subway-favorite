package wooteco.subway.domain.member;

import java.util.Objects;
import java.util.stream.Stream;

public class Favorite {
    private Long departStationId;
    private Long arriveStationId;

    private Favorite() {
    }

    public Favorite(Long departStationId, Long arriveStationId) {
        this.departStationId = departStationId;
        this.arriveStationId = arriveStationId;
    }

    public Stream<Long> getStationIdsStream() {
        return Stream.of(departStationId, arriveStationId);
    }

    public Long getDepartStationId() {
        return departStationId;
    }

    public Long getArriveStationId() {
        return arriveStationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Favorite favorite = (Favorite)o;
        return Objects.equals(departStationId, favorite.departStationId) &&
                Objects.equals(arriveStationId, favorite.arriveStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departStationId, arriveStationId);
    }
}
