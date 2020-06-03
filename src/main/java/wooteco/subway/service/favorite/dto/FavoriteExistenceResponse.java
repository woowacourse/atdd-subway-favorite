package wooteco.subway.service.favorite.dto;

import java.util.Objects;

public class FavoriteExistenceResponse {
    private boolean existence;

    private FavoriteExistenceResponse() {
    }

    public FavoriteExistenceResponse(boolean existence) {
        this.existence = existence;
    }

    public boolean isExistence() {
        return existence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FavoriteExistenceResponse that = (FavoriteExistenceResponse)o;
        return existence == that.existence;
    }

    @Override
    public int hashCode() {
        return Objects.hash(existence);
    }
}
