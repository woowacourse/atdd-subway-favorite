package wooteco.subway.service.path.dto;

import wooteco.subway.domain.path.PathType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PathRequest {
    @NotEmpty
    private String source;
    @NotEmpty
    private String target;
    @NotNull
    private PathType type;

    private PathRequest() {
    }

    public PathRequest(final String source, final String target, final PathType type) {
        this.source = source;
        this.target = target;
        this.type = type;
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
