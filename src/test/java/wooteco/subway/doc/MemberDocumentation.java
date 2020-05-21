package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

public class MemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return document("members/create",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("The user's e-mail"),
                        fieldWithPath("name").type(JsonFieldType.STRING)
                                .description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING)
                                .description("The user's password")
                ));
    }

    public static RestDocumentationResultHandler findMember() {
        return document("members/find");
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("members/update",
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING)
                                .description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING)
                                .description("The user's password")
                ),
                requestHeaders(
                        headerWithName("Authorization").description(
                                "The token for login which is Bearer Type")
                )
        );
    }
}
