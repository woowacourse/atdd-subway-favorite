package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;
import wooteco.subway.domain.member.Member;
import wooteco.subway.web.member.InvalidAuthenticationException;

public class Favorite {
    @Id
    private Long id;
    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {
    }

    public Favorite(Long memberId, Long sourceStationId, Long targetStationId) {
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Favorite(Long id, Long memberId, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
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

    public void checkMember(Member member) {
        if (memberId != member.getId()) {
            throw new InvalidAuthenticationException("즐겨찾기를 등록한 사용자가 아님");
        }
    }
}
