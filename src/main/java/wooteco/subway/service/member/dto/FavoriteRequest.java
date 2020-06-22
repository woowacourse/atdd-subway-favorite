package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotBlank;

public class FavoriteRequest {
    @NotBlank(message = "EMPTY_SOURCE")
    private String sourceName;
    @NotBlank(message = "EMPTY_DESTINATION")
    private String destinationName;

    private FavoriteRequest() {
    }

    public FavoriteRequest(String sourceName, String destinationName) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }
}
