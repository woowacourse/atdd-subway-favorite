package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class LoginMemberDocumentation {

    public static RestDocumentationResultHandler createMember() {
        return document("members/create",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            )
        );
    }

    public static RestDocumentationResultHandler login() {
        return document("members/login",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseFields(
                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                    .description("The user's access token"),
                fieldWithPath("tokenType").type(JsonFieldType.STRING)
                    .description("The user's token type")
            )
        );
    }

    public static RestDocumentationResultHandler getMember() {
        return document("members/read",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
            )
        );
    }

    public static RestDocumentationResultHandler editMember() {
        return document("members/update",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            )
        );
    }

    public static RestDocumentationResultHandler deleteMember() {
        return document("members/delete",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            )
        );
    }
}
