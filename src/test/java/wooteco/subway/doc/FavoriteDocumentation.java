package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static wooteco.subway.doc.ApiDocumentUtils.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler addFavorite() {
        return docsTemplate("favorites/add",
            requestHeaders(
                headerWithName("Authorization").description("사용자의 JWT 토큰")
            ),
            requestFields(
                fieldWithPath("source").type(JsonFieldType.NUMBER).description("출발역의 ID"),
                fieldWithPath("target").type(JsonFieldType.NUMBER).description("도착역의 ID")
            )
        );
    }

    public static RestDocumentationResultHandler addFavoriteWithoutAuth() {
        return docsTemplate("favorites/addWithoutAuth",
            requestFields(
                fieldWithPath("source").type(JsonFieldType.NUMBER).description("출발역의 ID"),
                fieldWithPath("target").type(JsonFieldType.NUMBER).description("도착역의 ID")
            ),
            responseFields(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("에러 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
            )
        );
    }

    public static RestDocumentationResultHandler getAllFavorites() {
        return docsTemplate("favorites/getAll",
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
        return docsTemplate("favorites/remove",
            requestHeaders(
                headerWithName("Authorization").description("사용자의 JWT 토큰")
            ),
            pathParameters(
                parameterWithName("id").description("즐겨찾기의 고유 ID")
            )
        );
    }
}
