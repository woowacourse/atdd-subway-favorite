package wooteco.subway.domain.member;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.common.BaseEntity;
import wooteco.subway.domain.favorite.Favorite;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@AttributeOverride(name = "id", column = @Column(name = "MEMBER_ID"))
public class Member extends BaseEntity {

    @Column(name = "MEMBER_NAME")
    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Favorite> favorites;

    public Member(Long id, String name, String email, String password) {
        this(email, name, password, new ArrayList<>());
        super.id = id;
    }

    public static Member of(String name, String email, String password) {
        return new Member(name, email, password, new ArrayList<>());
    }

    public static Member of() {
        return new Member();
    }

    public void update(String name, String password) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
        if (StringUtils.isNotBlank(password)) {
            this.password = password;
        }
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isSameId(Member member) {
        return this.getId().equals(member.getId());
    }

    public void addFavorites(Favorite favorite) {
        favorites.add(favorite);
        favorite.applyFavorite(this);
    }
}
