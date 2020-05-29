package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler addFavorite() {
        return document("favorites/add",
                pathParameters(
                        parameterWithName("id").description("즐겨찾기를 추가할 Member의 id")
                ),
                requestFields(
                        fieldWithPath("sourceId").type(JsonFieldType.STRING).description("즐겨찾기 출발역 이름"),
                        fieldWithPath("targetId").type(JsonFieldType.STRING).description("즐겨찾기 도착역 이름")
                ),
                responseHeaders(
                        headerWithName("Location").description("즐겨찾기 정보가 생성된 location")
                )
        );
    }

    public static RestDocumentationResultHandler readFavorites() {
        return document("favorites/read",
                pathParameters(
                        parameterWithName("id").description("즐겨찾기를 조회할 Member의 id")
                )
        );
    }

    public static RestDocumentationResultHandler deleteFavorite() {
        return document("favorites/delete",
                pathParameters(
                        parameterWithName("memberId").description("즐겨찾기를 삭제할 Member의 id"),
                        parameterWithName("sourceId").description("삭제할 즐겨찾기 출발역 id"),
                        parameterWithName("targetId").description("삭제할 즐겨찾기 도착역 id")
                )
        );
    }
}
