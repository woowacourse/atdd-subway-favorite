package wooteco.subway.docs;


import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler createFavorite() {
        return document("user/favorite/create",
                requestHeaders(
                        headerWithName("Authorization").description("This is token which is Bearer Type")
                ),
                requestFields(
                        fieldWithPath("sourceId").type(JsonFieldType.NUMBER).description("The source station id"),
                        fieldWithPath("targetId").type(JsonFieldType.NUMBER).description("The target station id")
                )
        );
    }

    public static RestDocumentationResultHandler getAllFavorites() {
        return document("user/favorite/getAll",
                requestHeaders(
                        headerWithName("Authorization").description("This is token which is Bearer Type")
                ),
                responseFields(
                        fieldWithPath("[].sourceName").type(JsonFieldType.STRING).description("The source station name"),
                        fieldWithPath("[].targetName").type(JsonFieldType.STRING).description("The target station name")
                )
        );
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("user/favorite/delete",
                requestHeaders(
                        headerWithName("Authorization").description("This is token which is Bearer Type")
                ),
                requestFields(
                        fieldWithPath("sourceName").type(JsonFieldType.STRING).description("The source station name"),
                        fieldWithPath("targetName").type(JsonFieldType.STRING).description("The target station name")
                )
        );
    }
}
