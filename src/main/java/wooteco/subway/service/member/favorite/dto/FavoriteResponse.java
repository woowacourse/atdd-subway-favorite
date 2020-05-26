package wooteco.subway.service.member.favorite.dto;

import wooteco.subway.domain.member.favorite.Favorite;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }

    public static List<FavoriteResponse> listOf(Set<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
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
