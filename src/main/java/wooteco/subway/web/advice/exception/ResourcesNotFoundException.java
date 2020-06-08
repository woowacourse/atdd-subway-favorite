package wooteco.subway.web.advice.exception;

import wooteco.subway.web.advice.exception.BusinessException;

public class ResourcesNotFoundException extends BusinessException {
    public ResourcesNotFoundException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
