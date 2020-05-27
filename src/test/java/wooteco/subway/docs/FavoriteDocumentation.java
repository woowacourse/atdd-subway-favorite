package wooteco.subway.docs;


import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler createFavorite() {
        return document("user/favorite/create",
                requestHeaders(
                        headerWithName("Authorization").description("This is token which is Bearer Type")
                ),
                requestFields(
                        fieldWithPath("source").type(JsonFieldType.STRING).description("The source station name"),
                        fieldWithPath("target").type(JsonFieldType.STRING).description("The target station name")
                )
        );
    }

    public static RestDocumentationResultHandler getAllFavorites() {
        return document("user/favorite/getAll",
                requestHeaders(
                        headerWithName("Authorization").description("This is token which is Bearer Type")
                ),
                responseFields(
                        fieldWithPath("[].source").type(JsonFieldType.STRING).description("The source station name"),
                        fieldWithPath("[].target").type(JsonFieldType.STRING).description("The target station name")
                )
        );
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("user/favorite/delete",
                requestHeaders(
                        headerWithName("Authorization").description("This is token which is Bearer Type")
                ),
                requestFields(
                        fieldWithPath("source").type(JsonFieldType.STRING).description("The source station name"),
                        fieldWithPath("target").type(JsonFieldType.STRING).description("The target station name")
                )
        );
    }
}
