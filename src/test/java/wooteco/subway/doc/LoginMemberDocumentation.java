package wooteco.subway.doc;

import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class LoginMemberDocumentation {
    public static ResultHandler login() {
        return document("/oauth/token");
    }
}
