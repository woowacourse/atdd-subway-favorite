package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler createFavorite() {
        return document("favorites/create",
                requestFields(
                        fieldWithPath("source").type(JsonFieldType.NUMBER).description("This is favorite source station id"),
                        fieldWithPath("target").type(JsonFieldType.NUMBER).description("This is favorite target station id")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler getFavorite() {
        return document("favorites/get",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                ),
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("This is favorite's id"),
                        fieldWithPath("[].memberId").type(JsonFieldType.NUMBER).description("This is user's id"),
                        fieldWithPath("[].sourceStationId").type(JsonFieldType.NUMBER).description("This is favorite source station id"),
                        fieldWithPath("[].targetStationId").type(JsonFieldType.NUMBER).description("This is favorite target station id"),
                        fieldWithPath("[].sourceStationName").type(JsonFieldType.STRING).description("This is favorite source station name"),
                        fieldWithPath("[].targetStationName").type(JsonFieldType.STRING).description("This is favorite target station name")
                )
        );
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("favorites/delete",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }
}
