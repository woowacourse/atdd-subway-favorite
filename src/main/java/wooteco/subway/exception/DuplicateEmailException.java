package wooteco.subway.exception;

public class DuplicateEmailException extends SubwayException{
    public DuplicateEmailException() {
        super("동일한 이메일의 계정이 이미 존재합니다.");
    }
}
