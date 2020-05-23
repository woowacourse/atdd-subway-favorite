package wooteco.subway.service.favorite.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteResponse {

    private Long id;
    private Long memberId;
    private String source;
    private String target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long memberId, String source, String target) {
        this.id = id;
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getMemberId(), favorite.getSource(),
            favorite.getTarget());
    }

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
