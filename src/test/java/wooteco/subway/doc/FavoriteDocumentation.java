package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class FavoriteDocumentation {
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
