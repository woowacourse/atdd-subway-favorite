package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberDocumentation {

    static final HeaderDescriptor HEADER_AUTHORIZATION = headerWithName(
        "authorization").description("The user's authorized token");

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

    public static RestDocumentationResultHandler getMember() {
        return document("members/read",
            requestHeaders(
                HEADER_AUTHORIZATION
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Authorized user's id"),
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("Authorized user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("Authorized user's name")
            )
        );
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("members/update",
            requestHeaders(
                HEADER_AUTHORIZATION
            ),
            requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Authorized user's id"),
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("Authorized user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("Authorized user's name")
            )
        );
    }

    public static RestDocumentationResultHandler deleteMember() {
        return document("members/delete",
            requestHeaders(
                HEADER_AUTHORIZATION
            )
        );
    }
}
