package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.NotNull;

public class FavoriteCreateRequest {
    @NotNull
    private Long source;
    @NotNull
    private Long target;

    public FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(Long memberId, Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
