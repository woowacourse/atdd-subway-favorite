package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

public class Favorite {
    @Id
    private Long id;

    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    @Transient
    private String sourceStationName;

    @Transient
    private String targetStationName;

    private Favorite() {
    }

    public Favorite(final Long id, final Long memberId, final Long sourceStationId, final Long targetStationId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Favorite(final Long memberId, final Long sourceStationId, final Long targetStationId) {
        this(null, memberId, sourceStationId, targetStationId);
    }

    public void updateStationsName(final String sourceStationName, final String targetStationName) {
        this.sourceStationName = sourceStationName;
        this.targetStationName = targetStationName;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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
