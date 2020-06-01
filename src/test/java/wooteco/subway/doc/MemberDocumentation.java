package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return document("members/create",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseHeaders(
                headerWithName("Location").description("The user's location who just created")
            )
        );
    }

    public static RestDocumentationResultHandler loginMember() {
        return document("me/login",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseHeaders(
                headerWithName("Authorization").description(
                    "The user's authorization who just created")
            )
        );
    }

    public static RestDocumentationResultHandler getMemberByEmail() {
        return document("me/getMemberByEmail",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login member which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER)
                    .description("The user's id"),
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email"),
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("The user's name")
            )
        );
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("me/update",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login member which is Bearer Type")
            ),
            requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            )
        );
    }

    public static RestDocumentationResultHandler deleteMember() {
        return document("me/deleteWithAuth",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login member which is Bearer Type")
            )
        );
    }

    public static RestDocumentationResultHandler failedCreateMemberByBlank() {
        return document("me/create/fail/blank",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseFields(
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("The error message")
            )
        );
    }

    public static RestDocumentationResultHandler failedCreateMemberByDuplication() {
        return document("me/create/fail/duplication",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseFields(
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("The error message")
            )
        );
    }

    public static RestDocumentationResultHandler failedUpdateMemberByBlank() {
        return document("me/update/fail/blank",
            requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseFields(
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("The error message")
            )
        );
    }

    public static RestDocumentationResultHandler addFavorite() {
        return document("me/favorites/add",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login member which is Bearer Type")
            ),
            requestFields(
                fieldWithPath("departureId").type(JsonFieldType.NUMBER)
                    .description("The favorite's departure id"),
                fieldWithPath("destinationId").type(JsonFieldType.NUMBER)
                    .description("The favorite's destination id")
            ),
            responseHeaders(
                headerWithName("Location").description("The created favorite's location")
            )
        );
    }

    public static RestDocumentationResultHandler findFavorites() {
        return document("me/favorites/find",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login member which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("favoriteResponses[]").description("The array of favorite items"),
                fieldWithPath("favoriteResponses[].id").description("The favorite's id"),
                fieldWithPath("favoriteResponses[].departureId").description(
                    "The favorite's departure id"),
                fieldWithPath("favoriteResponses[].destinationId").description(
                    "The favorite's destination id")
            )
        );
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("me/favorites/deleteWithAuth",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login member which is Bearer Type")
            )
        );
    }
}
