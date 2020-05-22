package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class LoginMemberDocumentation {
    public static RestDocumentationResultHandler login() {
        return document("me/login",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseFields(
                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                    .description("The token used for authenticating user"),
                fieldWithPath("tokenType").type(JsonFieldType.STRING)
                    .description("The token type used for login is Bearer")
            )
        );
    }

    public static RestDocumentationResultHandler getMemberOfMine() {
        return document("me/read",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER)
                    .description("The user's identity"),
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("The user's name")
            )
        );
    }

    public static RestDocumentationResultHandler updateMemberOfMine() {
        return document("me/update",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("The user's updated name")
                    .optional(),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's updated password")
                    .optional()
            )
        );
    }

    public static RestDocumentationResultHandler deleteMemberOfMine() {
        return document("me/delete",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            )
        );
    }
}
