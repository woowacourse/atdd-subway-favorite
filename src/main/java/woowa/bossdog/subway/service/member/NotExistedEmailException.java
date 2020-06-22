package woowa.bossdog.subway.service.member;

import woowa.bossdog.subway.web.advice.exception.ResourcesNotFoundException;

public class NotExistedEmailException extends ResourcesNotFoundException {
    private static final String ERROR_CODE = "NOT_EXISTED_EMAIL";

    public NotExistedEmailException() {
        super(ERROR_CODE, "등록되지 않은 이메일입니다.");
    }
}
