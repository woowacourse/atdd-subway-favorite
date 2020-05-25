package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Favorite;

import java.util.Set;

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


    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
