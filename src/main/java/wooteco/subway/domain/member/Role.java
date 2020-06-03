package wooteco.subway.domain.member;

public enum Role {
    ADMIN(1),
    USER(2);

    private final int grade;

    Role(int grade) {
        this.grade = grade;
    }

    public boolean isLessThan(Role role) {
        return grade > role.grade;
    }
}
