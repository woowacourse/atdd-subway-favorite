package woowa.bossdog.subway.service.path.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PathRequest {

    private String source;
    private String target;
    private PathType type;

    public PathRequest(final String source, final String target, final PathType type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }
}
