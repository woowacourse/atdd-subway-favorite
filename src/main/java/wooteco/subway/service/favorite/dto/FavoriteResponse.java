package wooteco.subway.service.favorite.dto;

public class FavoriteResponse {
    private Long id;
    private String source;
    private String target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, String source, String target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
