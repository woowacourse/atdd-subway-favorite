package wooteco.subway.service.favorite.dto;

public class CreateFavoriteRequest {
    private String source;
    private String target;
    private String email;

    public CreateFavoriteRequest() {
    }

    public CreateFavoriteRequest(String source, String target, String email) {
        this.source = source;
        this.target = target;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
