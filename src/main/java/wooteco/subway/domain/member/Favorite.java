package wooteco.subway.domain.member;

import java.util.Objects;

public class Favorite {
    private String source;
    private String target;

    public Favorite() {
    }

    public Favorite(String source, String target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
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
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(getSource(), favorite.getSource()) &&
                Objects.equals(getTarget(), favorite.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getTarget());
    }
}
