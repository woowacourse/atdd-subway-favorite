package wooteco.subway.domain.member.favorite;

public class Favorite {
    private Long id;
    private String source;
    private String target;

    public Favorite() {
    }

    public Favorite(Long id, String source, String target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Favorite(String source, String target) {
        this(null, source, target);
    }

    public Long getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public boolean isSame(Favorite favorite) {
        return this.source.equals(favorite.source) && this.target.equals(favorite.target);
    }

    public boolean isSameId(Long id) {
        return this.id.equals(id);
    }
}
