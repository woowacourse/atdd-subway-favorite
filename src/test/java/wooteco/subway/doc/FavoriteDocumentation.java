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
                fieldWithPath("source").type(JsonFieldType.NUMBER).description("The source id"),
                fieldWithPath("target").type(JsonFieldType.NUMBER).description("The target id")
            )
        );
    }

    public static ResultHandler findFavorites() {
        return document("favorites/find",
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            ),
            responseFields(
                fieldWithPath("favoriteResponses[0].id").type(JsonFieldType.NUMBER)
                    .description("The favorite's id"),
                fieldWithPath("favoriteResponses[0].memberId").type(JsonFieldType.NUMBER)
                    .description("The favorite's memberId"),
                fieldWithPath("favoriteResponses[0].sourceId").type(JsonFieldType.NUMBER)
                    .description("The favorite's sourceId"),
                fieldWithPath("favoriteResponses[0].targetId").type(JsonFieldType.NUMBER)
                    .description("The favorite's targetId"),
                fieldWithPath("favoriteResponses[0].sourceName").type(JsonFieldType.STRING)
                    .description("The favorite's sourceName"),
                fieldWithPath("favoriteResponses[0].targetName").type(JsonFieldType.STRING)
                    .description("The favorite's targetName")
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
