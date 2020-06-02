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
    public static ResultHandler select() {
        return document("favorite/select",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static ResultHandler create() {
        return document("favorite/create",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                ),
                requestFields(
                        fieldWithPath("source").type(JsonFieldType.STRING).description("The source station name"),
                        fieldWithPath("target").type(JsonFieldType.STRING).description("The target station name")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The favorite's id"),
                        fieldWithPath("source").type(JsonFieldType.STRING).description("The favorite's source station name"),
                        fieldWithPath("target").type(JsonFieldType.STRING).description("The favorite's target station name")
                )
        );
    }

    public static ResultHandler delete() {
        return document("favorite/delete",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                ),
                pathParameters(
                        parameterWithName("id").description("삭제할 즐겨찾기 아이디")
                )
        );
    }
}
