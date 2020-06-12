package wooteco.subway.service.member.dto;

public class FavoriteResponse {
    private String sourceName;
    private String destinationName;

    private FavoriteResponse() {
    }

    public FavoriteResponse(String sourceName, String destinationName) {
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
