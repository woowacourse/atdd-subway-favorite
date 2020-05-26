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
                headerWithName("Authorization").description("사용자의 JWT 토큰")
            ),
            requestFields(
                fieldWithPath("source").type(JsonFieldType.NUMBER).description("출발역의 ID"),
                fieldWithPath("target").type(JsonFieldType.NUMBER).description("도착역의 ID")
            )
        );
    }

    public static RestDocumentationResultHandler getAllFavorites() {
        return document("favorites/getAll",
            requestHeaders(
                headerWithName("Authorization").description("사용자의 JWT 토큰")
            ),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("즐겨찾기의 ID"),
                fieldWithPath("[].sourceStationId").type(JsonFieldType.NUMBER).description("출발역의 ID"),
                fieldWithPath("[].targetStationId").type(JsonFieldType.NUMBER).description("도착역의 ID"),
                fieldWithPath("[].sourceStationName").type(JsonFieldType.STRING).description("출발역의 이름"),
                fieldWithPath("[].targetStationName").type(JsonFieldType.STRING).description("도착역의 이름")
            )
        );
    }

    public static RestDocumentationResultHandler removeFavorite() {
        return document("favorites/remove",
            requestHeaders(
                headerWithName("Authorization").description("사용자의 JWT 토큰")
            ),
            pathParameters(
                parameterWithName("id").description("즐겨찾기의 고유 ID")
            )
        );
    }
}
