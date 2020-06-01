package wooteco.subway.service.favorite.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.path.FavoritePathDto;
import wooteco.subway.service.station.dto.StationResponse;

public class FavoritePathResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoritePathResponse() {
    }

    public FavoritePathResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static List<FavoritePathResponse> listOf(List<FavoritePathDto> favoritePathDtos) {
        return favoritePathDtos.stream()
            .map(path -> new FavoritePathResponse(path.getId(), StationResponse.of(path.getSource()),
                StationResponse.of(path.getTarget())))
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
