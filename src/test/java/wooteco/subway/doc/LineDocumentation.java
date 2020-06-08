package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class LineDocumentation {
    public static RestDocumentationResultHandler createLine() {
        return document("lines/create",
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("This is line name"),
                        fieldWithPath("startTime").type(JsonFieldType.STRING).description("This is start time"),
                        fieldWithPath("endTime").type(JsonFieldType.STRING).description("This is end time"),
                        fieldWithPath("intervalTime").type(JsonFieldType.NUMBER).description("This is interval time")
                )
        );
    }

    public static RestDocumentationResultHandler getLines() {
        return document("lines/getAll",
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("This is line id"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("This is line name"),
                        fieldWithPath("[].startTime").type(JsonFieldType.STRING).description("This is start time"),
                        fieldWithPath("[].endTime").type(JsonFieldType.STRING).description("This is end time"),
                        fieldWithPath("[].intervalTime").type(JsonFieldType.NUMBER).description("This is interval time"),
                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("This is created time"),
                        fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("This is updated time")
                )
        );
    }

    public static RestDocumentationResultHandler getLine() {
        return document("lines/get",
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("This is line id"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("This is line name"),
                        fieldWithPath("startTime").type(JsonFieldType.STRING).description("This is start time"),
                        fieldWithPath("endTime").type(JsonFieldType.STRING).description("This is end time"),
                        fieldWithPath("intervalTime").type(JsonFieldType.NUMBER).description("This is interval time"),
                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("This is created time"),
                        fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("This is updated time"),
                        fieldWithPath("stations.[].id").type(JsonFieldType.NUMBER).description("This is stations id"),
                        fieldWithPath("stations.[].name").type(JsonFieldType.STRING).description("This is stations name"),
                        fieldWithPath("stations.[].createdAt").type(JsonFieldType.STRING).description("This is station created time")
                )
        );
    }

    public static RestDocumentationResultHandler updateLine() {
        return document("lines/update",
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("This is line name"),
                        fieldWithPath("startTime").type(JsonFieldType.STRING).description("This is start time"),
                        fieldWithPath("endTime").type(JsonFieldType.STRING).description("This is end time"),
                        fieldWithPath("intervalTime").type(JsonFieldType.NUMBER).description("This is interval time")
                )
        );
    }

    public static RestDocumentationResultHandler deleteLine() {
        return document("lines/delete");
    }
}
