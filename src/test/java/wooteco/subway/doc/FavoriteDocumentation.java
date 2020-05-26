package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class FavoriteDocumentation {

    public static RestDocumentationResultHandler addFavorite() {
        return document("favorites/add",
            requestHeaders(
                headerWithName("Authorization").description("The login token should be bearer type")
            ),
            requestFields(
                fieldWithPath("source").type(JsonFieldType.NUMBER)
                    .description("The source station id"),
                fieldWithPath("target").type(JsonFieldType.NUMBER)
                    .description("The target station id")
            ),
            responseHeaders(
                headerWithName("Location").description("The Favorite's location just created")
            )
        );
    }

    public static RestDocumentationResultHandler getFavorite() {
        return document("favorites/get",
            requestHeaders(
                headerWithName("Authorization").description("The login token should be bearer type")
            ),
            pathParameters(
                parameterWithName("source").description("The source station id"),
                parameterWithName("target").description("The target station id")
            ),
            responseBody()
        );
    }

    public static RestDocumentationResultHandler getFavorites() {
        return document("favorites/read",
            requestHeaders(
                headerWithName("Authorization").description("The login token should be bearer type")
            ),
            responseBody()
        );
    }
}
