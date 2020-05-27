package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathDocumentation {
    public static RestDocumentationResultHandler findPath() {
        return document("paths/find",
                requestParameters(
                        parameterWithName("source").description("source station name"),
                        parameterWithName("target").description("target station name"),
                        parameterWithName("type").description("search criteria, Distance or Duration")
                ),
                responseFields(
                        fieldWithPath("stations.[].id").type(JsonFieldType.NUMBER).description("station id"),
                        fieldWithPath("stations.[].name").type(JsonFieldType.STRING).description("station name"),
                        fieldWithPath("stations.[].createdAt").type(JsonFieldType.STRING).description("station created time"),
                        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("distance cost"),
                        fieldWithPath("duration").type(JsonFieldType.NUMBER).description("duration cost")
                )
        );
    }
}
