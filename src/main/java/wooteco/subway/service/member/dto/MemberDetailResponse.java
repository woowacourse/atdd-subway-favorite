package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Member;

import java.util.Set;

public class MemberDetailResponse {
    private Long id;
    private String email;
    private String name;
    private Set<FavoriteResponse> favorites;

    public MemberDetailResponse() {
    }

    public MemberDetailResponse(Long id, String email, String name, Set<FavoriteResponse> favorites) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.favorites = favorites;
    }

    public static MemberDetailResponse of(Member member, Set<FavoriteResponse> favorites) {
        return new MemberDetailResponse(member.getId(), member.getEmail(), member.getName(), favorites);
    }
}
