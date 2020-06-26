package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class AuthDocumentation {
    public static RestDocumentationResultHandler failToAuthorizeMemberByToken() {
        return document("authorization/authorize_fail_token");
    }

    public static RestDocumentationResultHandler failToAuthorizeMemberBySession() {
        return document("authorization/authorize_fail_session");
    }
}
