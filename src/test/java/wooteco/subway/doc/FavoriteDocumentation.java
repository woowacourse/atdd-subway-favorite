package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler createFavorite() {
        return document("favorites/create",
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            ),
            requestFields(
                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("The member's id"),
                fieldWithPath("source").type(JsonFieldType.STRING).description("The favorite's start station"),
                fieldWithPath("target").type(JsonFieldType.STRING).description("The favorite's arrival station")
            )
        );
    }

    public static ResultHandler findFavorites() {
        return document("favorites/find",
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("favoriteStations[0].memberId").type(JsonFieldType.NUMBER).description("The user's favorites set"),
                fieldWithPath("favoriteStations[0].source").type(JsonFieldType.STRING).description("The user's favorites set"),
                fieldWithPath("favoriteStations[0].target").type(JsonFieldType.STRING).description("The user's favorites set")
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
