package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler createFavorite() {
        return document("favorites/create",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                ),
                requestFields(
                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("The member's id"),
                        fieldWithPath("source").type(JsonFieldType.NUMBER).description("The favorite's start station"),
                        fieldWithPath("target").type(JsonFieldType.NUMBER).description("The favorite's arrival station")
                )
        );
    }

    public static ResultHandler findFavorites() {
        return document("favorites/find",
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            ),
            responseFields(
                    fieldWithPath("favoriteStations[0].memberId").type(JsonFieldType.NUMBER)
                            .description("The user's favorites set"),
                    fieldWithPath("favoriteStations[].source.id").type(JsonFieldType.NUMBER)
                            .description("The user's favorites set"),
                    fieldWithPath("favoriteStations[].source.name").type(JsonFieldType.STRING)
                            .description("The user's favorites set"),
                    fieldWithPath("favoriteStations[].source.createdAt").type(JsonFieldType.STRING)
                            .description("The user's favorites set").optional(),
                    fieldWithPath("favoriteStations[].target.id").type(JsonFieldType.NUMBER)
                            .description("The user's favorites set"),
                    fieldWithPath("favoriteStations[].target.name").type(JsonFieldType.STRING)
                            .description("The user's favorites set"),
                    fieldWithPath("favoriteStations[].target.createdAt").type(JsonFieldType.STRING)
                            .description("The user's favorites set").optional()
            )
        );
    }

    public static ResultHandler deleteFavorite() {
        return document("favorites/delete",
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            )
        );
    }
}
