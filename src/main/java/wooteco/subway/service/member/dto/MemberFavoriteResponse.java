package wooteco.subway.service.member.dto;

import java.util.Set;

public class MemberFavoriteResponse {
    private Long id;
    private Set<FavoriteResponse> favorites;

    private MemberFavoriteResponse() {}

    public MemberFavoriteResponse(Long id, Set<FavoriteResponse> favorites) {
        this.id = id;
        this.favorites = favorites;
    }

    public static MemberFavoriteResponse of(Long id, Set<FavoriteResponse> favorites) {
        return new MemberFavoriteResponse(id, favorites);
    }

    public Long getId() {
        return id;
    }

    public Set<FavoriteResponse> getFavorites() {
        return favorites;
    }
}
