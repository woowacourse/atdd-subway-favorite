package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

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

    public static RestDocumentationResultHandler createFavorite() {
        return document("me/favorites/create",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            requestFields(
                fieldWithPath("sourceStationId").type(JsonFieldType.NUMBER)
                    .description("Departure Station"),
                fieldWithPath("targetStationId").type(JsonFieldType.NUMBER)
                    .description("Arrival Station")
            )
        );
    }

    public static RestDocumentationResultHandler getFavorites() {
        return document("me/favorites/read",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                    .description("The favorite's identity"),
                fieldWithPath("[].sourceId").type(JsonFieldType.NUMBER)
                    .description("The departure station's identity"),
                fieldWithPath("[].targetId").type(JsonFieldType.NUMBER)
                    .description("The target station's identity"),
                fieldWithPath("[].sourceName").type(JsonFieldType.STRING)
                    .description("The departure station's name"),
                fieldWithPath("[].targetName").type(JsonFieldType.STRING)
                    .description("The target station's name")
            ));
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("me/favorites/delete",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            pathParameters(
                parameterWithName("favoriteId")
                    .description("The favorite's identity")
            )
        );
    }

    public static RestDocumentationResultHandler hasFavorite() {
        return document("me/favorites/exist",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            pathParameters(
                parameterWithName("sourceId")
                    .description("The departure station's identity"),
                parameterWithName("targetId")
                    .description("The target station's identity")
            ),
            responseFields(
                fieldWithPath("exist").type(JsonFieldType.BOOLEAN)
                    .description("The status whether the favorite was already added or not")
            )

        );
    }
}
