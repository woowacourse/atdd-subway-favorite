package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static wooteco.subway.doc.ApiDocumentUtils.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler addFavorite() {
        return document("favorites/create",
            getDocumentRequest(),
            getDocumentResponse(),
            requestFields(
                fieldWithPath("sourceId").type(JsonFieldType.STRING)
                    .description("The Favorite source-id").attributes(),
                fieldWithPath("targetId").type(JsonFieldType.STRING)
                    .description("The Favorite targetId")
            ),
            responseHeaders(
                headerWithName("Location").description(
                    "The favorite path location which just created")
            )
        );
    }

    public static RestDocumentationResultHandler addFavoriteFail(String url) {
        return document(url,
            getDocumentRequest(),
            getDocumentResponse(),
            requestFields(
                fieldWithPath("sourceId").type(JsonFieldType.STRING)
                    .description("The Favorite source-id").attributes(),
                fieldWithPath("targetId").type(JsonFieldType.STRING)
                    .description("The Favorite targetId")
            ),
            responseFields(
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("The Error message")
            )
        );
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("favorites/delete",
            getDocumentRequest(),
            getDocumentResponse()
        );
    }

    public static RestDocumentationResultHandler deleteFavoriteFail(String url) {
        return document(url,
            getDocumentRequest(),
            getDocumentResponse(),
            responseFields(
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("The Error message")
            )
        );
    }
}
