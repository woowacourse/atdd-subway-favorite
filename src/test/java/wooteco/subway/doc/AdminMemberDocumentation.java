package wooteco.subway.doc;


import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class AdminMemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return document("admin/members/create",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                responseHeaders(
                        headerWithName("Location").description("The user's location who just created")
                )
        );
    }

    public static RestDocumentationResultHandler getMemberByEmail() {
        return document("admin/members/get",
                requestParameters(
                        parameterWithName("email").description("The user's email")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
                )
        );
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("admin/members/update",
                pathParameters(
                        parameterWithName("id").description("The user's id")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler deleteMember() {
        return document("admin/members/delete",
                pathParameters(
                        parameterWithName("id").description("The user's id")
                )
        );
    }
}
