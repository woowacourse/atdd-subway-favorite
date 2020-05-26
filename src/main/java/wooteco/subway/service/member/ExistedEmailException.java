package wooteco.subway.service.member;

public class ExistedEmailException extends RuntimeException {
    public ExistedEmailException() {
        super("이미 존재하는 이메일 입니다.");
    }
}
