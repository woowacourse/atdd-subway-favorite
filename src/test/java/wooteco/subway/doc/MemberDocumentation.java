package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

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
        return document("members/login",
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
        return document("members/getMemberByEmail",
                requestHeaders(
                        headerWithName("Authorization").description(
                                "The token for login member which is Bearer Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                .description("The user's id"),
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("The user's eamil"),
                        fieldWithPath("name").type(JsonFieldType.STRING)
                                .description("The user's name")
                )
        );
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("members/update",
                pathParameters(
                        parameterWithName("id").description("The user's id")),
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
        return document("members/delete",
                pathParameters(
                        parameterWithName("id").description("The user's id")),
                requestHeaders(
                        headerWithName("Authorization").description(
                                "The token for login member which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler failedCreateMemberByBlank() {
        return document("members/create/fail/blank",
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
        return document("members/create/fail/duplication",
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
        return document("members/update/fail/blank",
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
        return document("members/favorites/add",
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
        return document("members/favorites/find",
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
        return document("members/favorites/delete",
                requestHeaders(
                        headerWithName("Authorization").description(
                                "The token for login member which is Bearer Type")
                ),
                pathParameters(
                        parameterWithName("id").description("The favorite's id")
                )
        );
    }
}
