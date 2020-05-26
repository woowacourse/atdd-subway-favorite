package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class LoginMemberDocumentation {
    public static RestDocumentationResultHandler login() {
        return document("loginMember/login",
            requestHeaders(
                headerWithName("Authorization").description("The login token should be bearer type")
            ),
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The login email"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The login password")
            )
        );
    }

    public static RestDocumentationResultHandler getMemberOfMineBasic() {
        return document("loginMember/get",
            requestHeaders(
                headerWithName("Authorization").description("The login token should be bearer type")
            ),
            responseBody()
        );
    }

    public static RestDocumentationResultHandler updateMe() {
        return document("loginMember/update",
            requestHeaders(
                headerWithName("Authorization").description("The login token should be bearer type")
            ),
            requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("The update name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The update password")
            )
        );
    }

    public static RestDocumentationResultHandler deleteMe() {
        return document("loginMember/delete",
            requestHeaders(
                headerWithName("Authorization").description("The login token should be bearer type")
            )
        );
    }
}
