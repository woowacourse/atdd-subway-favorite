package wooteco.subway.service.favorite.dto;

public class FavoriteExistenceResponse {
    private boolean existence;

    private FavoriteExistenceResponse() {
    }

    public FavoriteExistenceResponse(boolean existence) {
        this.existence = existence;
    }

    public boolean isExistence() {
        return existence;
    }
}
