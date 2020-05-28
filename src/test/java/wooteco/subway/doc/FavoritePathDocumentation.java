package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class FavoritePathDocumentation {
    public static RestDocumentationResultHandler showAll() {
        return document("favorite-paths/show-all",
            requestHeaders(
                headerWithName("Authorization").description("Bearer auth credentials")
            ),
            responseFields(
                fieldWithPath("[].startStationName").type(JsonFieldType.STRING).description("name of start station"),
                fieldWithPath("[].endStationName").type(JsonFieldType.STRING).description("name of end station")
            )
        );
    }

    public static RestDocumentationResultHandler registerFavoritePath() {
        return document("favorite-paths/register-favorite-path",
            requestHeaders(
                headerWithName("Authorization").description("Bearer auth credentials")
            ),
            requestFields(
                fieldWithPath("startStationName").type(JsonFieldType.STRING).description("name of start station"),
                fieldWithPath("endStationName").type(JsonFieldType.STRING).description("name of end station")
            )
        );
    }

    public static RestDocumentationResultHandler deleteFavoritePath() {
        return document("favorite-paths/delete-favorite-path",
            requestHeaders(
                headerWithName("Authorization").description("Bearer auth credentials")
            ),
            requestFields(
                fieldWithPath("startStationName").type(JsonFieldType.STRING).description("name of start station"),
                fieldWithPath("endStationName").type(JsonFieldType.STRING).description("name of end station")
            )
        );
    }
}
