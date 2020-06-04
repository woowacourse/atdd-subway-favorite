package wooteco.subway.service.member.dto;

public class FavoriteExistResponse {
    private boolean exist;

    private FavoriteExistResponse() {
    }

    public FavoriteExistResponse(boolean exist) {
        this.exist = exist;
    }

    public boolean isExist() {
        return exist;
    }
}
