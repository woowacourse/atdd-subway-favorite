package wooteco.subway.doc;


import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;
import wooteco.subway.web.member.util.AuthorizationExtractor;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class MemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return document("members/create",
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

    public static ResultHandler deleteMember() {
        return document("members/delete",
                requestHeaders(
                        headerWithName(AuthorizationExtractor.AUTHORIZATION).description("The token for login which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("members/update",
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                requestHeaders(
                        headerWithName(AuthorizationExtractor.AUTHORIZATION).description("The token for login which is Bearer Type")
                )
        );
    }

    public static ResultHandler selectMember() {
        return document("members/select",
                requestParameters(
                        parameterWithName("email").description("조회할 유저의 이메일")
                ),
                requestHeaders(
                        headerWithName(AuthorizationExtractor.AUTHORIZATION).description("The token for login which is Bearer Type")
                )
        );
    }
}
