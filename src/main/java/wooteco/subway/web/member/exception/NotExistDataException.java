package wooteco.subway.web.member.exception;

import java.util.NoSuchElementException;

public class NotExistDataException extends NoSuchElementException {
    public NotExistDataException(String s) {
        super(s);
    }
}
