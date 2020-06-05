package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler createFavorite() {
        return document("favorites/create",
            requestFields(
                fieldWithPath("source").type(JsonFieldType.NUMBER).description("source station id"),
                fieldWithPath("target").type(JsonFieldType.NUMBER).description("target station id")
            ),
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            )
        );
    }

    public static RestDocumentationResultHandler createFavoriteWithoutAuthentication() {
        return document("favorites/create",
            requestFields(
                fieldWithPath("source").type(JsonFieldType.NUMBER).description("source station id"),
                fieldWithPath("target").type(JsonFieldType.NUMBER).description("target station id")
            ),
            responseFields(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("error code"),
                fieldWithPath("errorMessage").type(JsonFieldType.STRING)
                    .description("error message")
            )
        );
    }

    public static RestDocumentationResultHandler getFavorites() {
        return document("favorites/get",
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("The favorite's id"),
                fieldWithPath("[].memberId").type(JsonFieldType.NUMBER)
                    .description("The user's id"),
                fieldWithPath("[].sourceStationId").type(JsonFieldType.NUMBER)
                    .description("Id of source station"),
                fieldWithPath("[].targetStationId").type(JsonFieldType.NUMBER).description("Id of target station"),
                fieldWithPath("[].sourceStationName").type(JsonFieldType.STRING).description("Name of source station"),
                fieldWithPath("[].targetStationName").type(JsonFieldType.STRING).description("Name of target station")
            )
        );
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("favorites/delete");
    }
}
