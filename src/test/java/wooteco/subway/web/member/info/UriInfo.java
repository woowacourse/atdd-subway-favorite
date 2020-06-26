package wooteco.subway.web.member.info;

import io.jsonwebtoken.lang.Objects;

public class UriInfo {
    private String uri;
    private Object[] pathVariables;

    private UriInfo(String uri, Object[] pathVariables) {
        this.uri = uri;
        this.pathVariables = pathVariables;
    }

    public static UriInfo of(String uri) {
        return of(uri, new Long[0]);
    }

    public static UriInfo of(String uri, Object[] pathVariables) {
        return new UriInfo(uri, pathVariables);
    }

    public boolean isPathEmpty() {
        return Objects.isEmpty(pathVariables);
    }

    public String getUri() {
        return uri;
    }

    public Object[] getPathVariables() {
        return pathVariables;
    }
}
