package wooteco.subway.domain.member;

public enum Role {
    ADMIN(0),
    USER(1);

    private final int grade;

    Role(int grade) {
        this.grade = grade;
    }

    public boolean isHigherThan(Role role) {
        return grade < role.grade;
    }
}
