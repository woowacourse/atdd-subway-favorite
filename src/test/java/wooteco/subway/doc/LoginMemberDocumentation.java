package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class LoginMemberDocumentation {
    public static RestDocumentationResultHandler login() {
        return document("login",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                responseFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("The user's accessToken"),
                        fieldWithPath("tokenType").type(JsonFieldType.STRING).description("The user's tokenType")
                )
        );
    }
}
