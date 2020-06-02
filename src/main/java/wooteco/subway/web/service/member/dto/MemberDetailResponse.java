package wooteco.subway.web.service.member.dto;

import java.util.Set;
import java.util.stream.Collectors;

import wooteco.subway.domain.member.Member;
import wooteco.subway.web.service.favorite.dto.FavoriteDetailResponse;
import wooteco.subway.web.service.favorite.dto.FavoriteResponse;

public class MemberDetailResponse {
    private Long id;
    private String email;
    private String name;
    private Set<FavoriteResponse> favorites;

    public MemberDetailResponse() {
    }

    public MemberDetailResponse(Long id, String email, String name,
        Set<FavoriteResponse> favorites) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.favorites = favorites;
    }

    public static MemberDetailResponse of(Member member, Set<FavoriteDetailResponse> favorites) {
        Set<FavoriteResponse> simpleResponses = favorites.stream()
            .map(FavoriteDetailResponse::toSimple)
            .collect(Collectors.toSet());
        return new MemberDetailResponse(member.getId(), member.getEmail(), member.getName(),
            simpleResponses);
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
