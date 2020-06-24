package wooteco.subway.service.favorite.dto;

public class CreateFavoriteRequest {
    private String source;
    private String target;

    public CreateFavoriteRequest() {
    }

    public CreateFavoriteRequest(String source, String target) {
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
