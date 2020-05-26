package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return document("members/create",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseHeaders(
                headerWithName("Location").description("The user's location who just created")
            )
        );
    }
}
