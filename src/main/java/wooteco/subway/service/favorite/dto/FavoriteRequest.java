package wooteco.subway.service.favorite.dto;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotBlank;

public class FavoriteRequest {

    @NotBlank
    private String sourceName;

    @NotBlank
    private String targetName;

    public FavoriteRequest() {
    }

    public FavoriteRequest(String sourceName, String targetName) {
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public List<String> toList() {
        return Arrays.asList(sourceName, targetName);
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }
}
