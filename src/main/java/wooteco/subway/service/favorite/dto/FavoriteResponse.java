package wooteco.subway.service.favorite.dto;

public class FavoriteResponse {
    private final String source;
    private final String target;

    public FavoriteResponse(String source, String target) {
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
