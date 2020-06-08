package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class FavoriteDocumentation {
    public static RestDocumentationResultHandler createFavorite() {
        return document("favorites/create",
                requestFields(
                        getRequestFieldDescriptors()
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler readFavorite() {
        return document("favorites/read",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                ),
                responseFields(
                        fieldWithPath("[].sourceName").type(JsonFieldType.STRING).description("The source station's name"),
                        fieldWithPath("[].destinationName").type(JsonFieldType.STRING).description("The destination station's name")
                )
        );
    }

    public static RestDocumentationResultHandler removeFavorite() {
        return document("favorites/remove",
                requestFields(
                        getRequestFieldDescriptors()
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler ifExists() {
        return document("favorites/ifexists",
                requestParameters(
                        parameterWithName("source").description("The source station's name"),
                        parameterWithName("destination").description("The destination station's name")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                ),
                responseFields(
                        fieldWithPath("exists").description("whether favorite that is searched exists or not")
                )
        );
    }

    private static FieldDescriptor[] getRequestFieldDescriptors() {
        return new FieldDescriptor[]{fieldWithPath("sourceName").type(JsonFieldType.STRING).description("The source station's name"),
                fieldWithPath("destinationName").type(JsonFieldType.STRING).description("The destination station's name")};
    }
}
