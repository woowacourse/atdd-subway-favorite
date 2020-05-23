package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;

public class Favorite {

    @Id
    private Long id;
    private String memberEmail;
    private String source;
    private String target;

    public Favorite() {

    }

    public Favorite(Long id, String source, String target, String memberEmail) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.memberEmail = memberEmail;
    }

    public Favorite(String source, String target, String memberEmail) {
        this.source = source;
        this.target = target;
        this.memberEmail = memberEmail;
    }

    public Long getId() {
        return id;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
