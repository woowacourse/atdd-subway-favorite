package wooteco.subway.exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super(String.format("%s은 가입되지 않은 이메일입니다!", email));
    }
}
