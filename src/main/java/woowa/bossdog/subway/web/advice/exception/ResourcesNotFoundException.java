package woowa.bossdog.subway.web.advice.exception;

public class ResourcesNotFoundException extends BusinessException {
    public ResourcesNotFoundException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
