package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler createFavorite() {
        return document("favorites/create",
            requestHeaders(
                headerWithName("Authorization").description("The token for create favorite which is Bearer Type")
            ),
            requestFields(
                fieldWithPath("departureId").type(JsonFieldType.STRING).description("The departure station id"),
                fieldWithPath("arrivalId").type(JsonFieldType.STRING).description("The arrival station id")
            ),
            responseHeaders(
                headerWithName("Location").description("The favorite's location which just created")
            )
        );
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("favorites/delete",
            requestHeaders(
                headerWithName("Authorization").description("The token for deletion, which is Bearer Type")
            ),
            requestFields(
                fieldWithPath("departureId").type(JsonFieldType.STRING).description("The departure station id"),
                fieldWithPath("arrivalId").type(JsonFieldType.STRING).description("The arrival station id")
            )
        );
    }

    public static RestDocumentationResultHandler showFavorites() {
        return document("favorites/show",
            requestHeaders(
                headerWithName("Authorization")
                        .description("The token for getting login member's favorite list, which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("All Favorites"),
                fieldWithPath("[].departureId").type(JsonFieldType.NUMBER).description("The departure station id"),
                fieldWithPath("[].arrivalId").type(JsonFieldType.NUMBER).description("The arrival station id")
            )
        );
    }
}
