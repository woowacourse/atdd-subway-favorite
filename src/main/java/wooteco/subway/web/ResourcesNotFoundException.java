package wooteco.subway.web;

public class ResourcesNotFoundException extends BusinessException {
    public ResourcesNotFoundException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
