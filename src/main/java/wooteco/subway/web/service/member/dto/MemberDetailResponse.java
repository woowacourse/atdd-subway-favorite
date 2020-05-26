package wooteco.subway.web.service.member.dto;

import java.util.Set;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.web.service.favorite.dto.FavoriteResponse;

public class MemberDetailResponse {
    private Long id;
    private String email;
    private String name;
    private Set<FavoriteResponse> favorites;

    public MemberDetailResponse() {
    }

    public MemberDetailResponse(Long id, String email, String name,
        Set<Favorite> favorites) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.favorites = FavoriteResponse.setOf(favorites);
    }

    public static MemberDetailResponse of(Member member) {
        return new MemberDetailResponse(member.getId(), member.getEmail(), member.getName(),
            member.getFavorites());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Set<FavoriteResponse> getFavorites() {
        return favorites;
    }
}
