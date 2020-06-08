package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class StationDocumentation {
    public static RestDocumentationResultHandler createStation() {
        return document("stations/create",
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("This is station name")
                )
        );
    }

    public static RestDocumentationResultHandler getStations() {
        return document("stations/getAll",
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("This is station id"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("This is station name"),
                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("This is station created time")
                ));
    }


    public static RestDocumentationResultHandler deleteStation() {
        return document("stations/delete");
    }
}
