package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class LoginMemberDocumentation {

    public static RestDocumentationResultHandler loginMember() {
        return document("members/login",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                )
        );
    }
}
