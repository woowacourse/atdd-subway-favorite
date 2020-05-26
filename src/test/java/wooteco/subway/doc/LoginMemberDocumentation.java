package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static wooteco.subway.doc.ApiDocumentUtils.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class LoginMemberDocumentation {
    public static RestDocumentationResultHandler loginMember() {
        return document("login/login",
            getDocumentRequest(),
            getDocumentResponse(),
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("null").optional(),
                fieldWithPath("message").type(JsonFieldType.STRING).description("null").optional(),
                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                    .description("JWT value"),
                fieldWithPath("data.tokenType").type(JsonFieldType.STRING)
                    .description("Authorization type")
            )
        );
    }

    public static RestDocumentationResultHandler loginFailEmail() {
        return document("login/loginFail/email",
            getDocumentRequest(),
            getDocumentResponse(),
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description(1100),
                fieldWithPath("message").type(JsonFieldType.STRING).description("등록되지 않은 이메일"),
                fieldWithPath("data").type(JsonFieldType.NULL).description("null").optional()
            )
        );
    }

    public static RestDocumentationResultHandler loginFailedPassword() {
        return document("login/loginFail/password",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description(1200),
                fieldWithPath("message").type(JsonFieldType.STRING).description("잘못된 패스워드"),
                fieldWithPath("data").type(JsonFieldType.NULL).description("null").optional()
            )
        );
    }

    public static RestDocumentationResultHandler getMyInfo() {
        return document("me/getMyInfo",
            getDocumentRequest(),
            getDocumentResponse(),
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NULL).description("null").optional(),
                fieldWithPath("message").type(JsonFieldType.NULL).description("null").optional(),
                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("The user's id"),
                fieldWithPath("data.name").type(JsonFieldType.STRING)
                    .description("The user's name"),
                fieldWithPath("data.email").type(JsonFieldType.STRING)
                    .description("The user's email")
            )
        );
    }

    public static RestDocumentationResultHandler deleteMyInfo() {
        return document("me/deleteMyInfo",
            getDocumentRequest(),
            getDocumentResponse(),
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            )
        );
    }

    public static RestDocumentationResultHandler updateMyInfo() {
        return document("me/updateMyInfo",
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

    public static RestDocumentationResultHandler createFavorite() {
        return document("me/favorites/create",
            getDocumentRequest(),
            getDocumentResponse(),
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            requestFields(
                fieldWithPath("sourceStationId").type(JsonFieldType.NUMBER).description("시작역 ID"),
                fieldWithPath("targetStationId").type(JsonFieldType.NUMBER).description("종착역 ID")
            )
        );
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("me/favorites/delete",
            getDocumentRequest(),
            getDocumentResponse(),
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            requestFields(
                fieldWithPath("sourceStationId").type(JsonFieldType.NUMBER).description("시작역 ID"),
                fieldWithPath("targetStationId").type(JsonFieldType.NUMBER).description("종착역 ID")
            )
        );
    }

    public static RestDocumentationResultHandler getFavorites() {
        return document("me/favorites/getAll",
            getDocumentRequest(),
            getDocumentResponse(),
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NULL).description("null").optional(),
                fieldWithPath("message").type(JsonFieldType.NULL).description("null").optional(),
                fieldWithPath("data.favoriteResponses[0].sourceStationId").type(
                    JsonFieldType.NUMBER).description("시작역 ID"),
                fieldWithPath("data.favoriteResponses[0].sourceStationName").type(
                    JsonFieldType.STRING).description("시작역 이름"),
                fieldWithPath("data.favoriteResponses[0].targetStationId").type(
                    JsonFieldType.NUMBER).description("도착역 ID"),
                fieldWithPath("data.favoriteResponses[0].targetStationName").type(
                    JsonFieldType.STRING).description("도착역 이름")
            )
        );
    }
}
