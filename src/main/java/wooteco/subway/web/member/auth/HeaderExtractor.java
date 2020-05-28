package wooteco.subway.web.member.auth;

import javax.servlet.http.HttpServletRequest;

public interface HeaderExtractor {
    String extract(HttpServletRequest request, String type);
}
