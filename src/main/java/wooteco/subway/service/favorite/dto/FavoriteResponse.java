package wooteco.subway.service.favorite.dto;

import java.util.Objects;

public class FavoriteResponse {
    private String source;
    private String target;

    private FavoriteResponse() {
    }

    public FavoriteResponse(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FavoriteResponse that = (FavoriteResponse) o;
        return Objects.equals(source, that.source) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }

    @Override
    public String toString() {
        return "FavoriteResponse{" +
                "source='" + source + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
