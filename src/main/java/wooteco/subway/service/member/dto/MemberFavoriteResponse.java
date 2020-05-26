package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Member;

import java.util.Set;

public class MemberFavoriteResponse {
    private Long id;
    private Set<FavoriteResponse> favorites;

    public MemberFavoriteResponse() {}

    public MemberFavoriteResponse(Long id, Set<FavoriteResponse> favorites) {
        this.id = id;
        this.favorites = favorites;
    }

    public Long getId() {
        return id;
    }

    public Set<FavoriteResponse> getFavorites() {
        return favorites;
    }
}
