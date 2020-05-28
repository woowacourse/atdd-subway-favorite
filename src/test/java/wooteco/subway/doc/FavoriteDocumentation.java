package wooteco.subway.doc;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class FavoriteDocumentation {
    public static ResultHandler getFavorites() {
        return document("me/favorites/get",
                requestHeaders(
                        headerWithName("Authorization").description("본인 즐겨찾기 조회에 사용할 토큰")
                ),
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("즐겨찾기의 id"),
                        fieldWithPath("[].sourceStationId").type(JsonFieldType.NUMBER).description("즐겨찾기 출발역의 id"),
                        fieldWithPath("[].targetStationId").type(JsonFieldType.NUMBER).description("즐겨찾기 도착역의 id"),
                        fieldWithPath("[].sourceStationName").type(JsonFieldType.STRING).description("즐겨찾기 출발역의 이름"),
                        fieldWithPath("[].targetStationName").type(JsonFieldType.STRING).description("즐겨찾기 도착역의 이름")
                )
        );
    }

    public static ResultHandler addFavorite() {
        return document("me/favorites/create",
                requestHeaders(
                        headerWithName("Authorization").description("본인 즐겨찾기 추가에 사용할 토큰")
                ),
                requestFields(
                        fieldWithPath("sourceStationName").type(JsonFieldType.STRING).description("생성할 즐겨찾기의 출발역 이름"),
                        fieldWithPath("targetStationName").type(JsonFieldType.STRING).description("생성할 즐겨찾기의 도착역 이름")
                )
        );
    }

    public static ResultHandler deleteFavorite() {
        return document("me/favorites/delete",
                requestHeaders(
                        headerWithName("Authorization").description("본인 즐겨찾기 삭제에 사용할 토큰")
                ),
                pathParameters(
                        parameterWithName("id").description("삭제할 즐겨찾기의 id")
                )
        );
    }
}
