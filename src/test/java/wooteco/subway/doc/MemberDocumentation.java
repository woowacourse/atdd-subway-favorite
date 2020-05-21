package wooteco.subway.doc;


import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class MemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return document("members/create",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                responseHeaders(
                        headerWithName("Location").description("The user's location who just created")
                )
        );
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("me/bearer/update",
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for update which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler deleteMember() {
        return document("me/bearer/delete",
                requestHeaders(
                        headerWithName("Authorization").description("The token for delete which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler loginMember() {
        return document("oauth/token/bearer",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                responseFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("The token value"),
                        fieldWithPath("tokenType").type(JsonFieldType.STRING).description("The token type")
                )
        );
    }
}
