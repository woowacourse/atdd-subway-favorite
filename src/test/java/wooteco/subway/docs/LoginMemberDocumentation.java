package wooteco.subway.docs;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class LoginMemberDocumentation {

    public static RestDocumentationResultHandler login() {
        return document("user/login",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                responseFields(
                        fieldWithPath("accessToken").description("The token for login"),
                        fieldWithPath("tokenType").description("The token type for login")
                )
        );
    }

    public static RestDocumentationResultHandler createMember() {
        return document("user/create",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                )
        );
    }

    public static RestDocumentationResultHandler getMember() {
        return document("user/get",
                requestHeaders(
                        headerWithName("Authorization").description("This is token which is Bearer Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
                )
        );
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("user/update",
                requestHeaders(
                        headerWithName("Authorization").description("This is token which is Bearer Type")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name for updating"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password for updating")
                )
        );
    }

    public static RestDocumentationResultHandler deleteMember() {
        return document("user/delete",
                requestHeaders(
                        headerWithName("Authorization").description("This is token which is Bearer Type")
                )
        );
    }
}
