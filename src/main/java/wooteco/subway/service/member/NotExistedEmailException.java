package wooteco.subway.service.member;

import wooteco.subway.web.ResourcesNotFoundException;

public class NotExistedEmailException extends ResourcesNotFoundException {
    private static final String ERROR_CODE = "NOT_EXISTED_EMAIL";

    public NotExistedEmailException() {
        super(ERROR_CODE, "존재하지 않는 이메일 입니다.");
    }
}
