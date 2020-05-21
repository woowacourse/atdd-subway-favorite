package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

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

    public static RestDocumentationResultHandler getMember() {
        return document("members/findByEmail",
            requestParameters(
                parameterWithName("email").description("사용자의 이메일")
            ),
            requestHeaders(
                headerWithName("Authorization").description("사용자의 JWT 토큰")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자의 고유 id"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("사용자의 이메일"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자의 이름")

            ));
    }

    public static RestDocumentationResultHandler updateMember() {
        return document("members/update",
            pathParameters(
                parameterWithName("id").description("사용자의 고유 id")
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
        return document("members/delete",
            pathParameters(
                parameterWithName("id").description("사용자의 고유 id")
            ),
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            )
        );
    }
}
