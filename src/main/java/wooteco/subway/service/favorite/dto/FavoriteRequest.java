package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.NotEmpty;

public class FavoriteRequest {
    @NotEmpty(message = "출발역이 비었습니다.")
    private String source;
    @NotEmpty(message = "도착역이 비었습니다.")
    private String target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(@NotEmpty(message = "출발역이 비었습니다.") String source,
        @NotEmpty(message = "도착역이 비었습니다.") String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
