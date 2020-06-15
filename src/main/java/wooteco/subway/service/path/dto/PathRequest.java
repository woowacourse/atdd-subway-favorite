package wooteco.subway.service.path.dto;

import wooteco.subway.domain.path.PathType;

import javax.validation.constraints.NotBlank;

public class PathRequest {
    @NotBlank
    private String source;
    @NotBlank
    private String target;
    @NotBlank
    private PathType type;

    private PathRequest() {}

    public PathRequest(String source, String target, PathType type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public static PathRequest of(String source, String target, PathType type) {
        return new PathRequest(source, target, type);
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public PathType getType() {
        return type;
    }
}
