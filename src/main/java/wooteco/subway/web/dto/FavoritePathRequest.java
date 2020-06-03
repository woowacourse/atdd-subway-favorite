package wooteco.subway.web.dto;

public class FavoritePathRequest {
    private String source;
    private String target;

    public FavoritePathRequest() {
    }

    public FavoritePathRequest(String source, String target) {
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
