package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;

public class Favorite {
    @Id
    private Long id;
    private Long source;
    private Long target;
    private Long memberId;

    public Favorite(Long id, Long memberId, Long source, Long target) {
        this.id = id;
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Long memberId, Long source, Long target) {
        return new Favorite(null, memberId, source, target);
    }

    public Long getId() {
        return id;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public Long getMemberId() {
        return memberId;
    }
}
