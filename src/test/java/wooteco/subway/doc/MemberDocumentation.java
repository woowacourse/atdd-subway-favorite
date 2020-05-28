package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static wooteco.subway.doc.ApiDocumentUtils.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberDocumentation {
    public static RestDocumentationResultHandler createMember() {
        return docsTemplate("members/create",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("사용자의 이메일"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자의 이름"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("사용자의 비밀번호")
            ),
            responseHeaders(
                headerWithName("Location").description("생성된 사용자의 Location URI")
            )
        );
    }

    public static RestDocumentationResultHandler createMemberWithEmptyFields() {
        return docsTemplate("members/createWithEmptyFields",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("사용자의 이메일"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자의 이름"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("사용자의 비밀번호")
            ),
            responseFields(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("에러 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
            )
        );
    }

    public static RestDocumentationResultHandler login() {
        return docsTemplate("members/login",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("사용자의 이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("사용자의 비밀번호")
            ),
            responseFields(
                fieldWithPath("tokenType").type(JsonFieldType.STRING).description("토큰 종류 (BEARER, BASIC...)"),
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("사용자 인증 토큰")
            )
        );
    }

    public static RestDocumentationResultHandler invalidLogin() {
        return docsTemplate("members/invalidLogin",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("사용자의 이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("사용자의 비밀번호")
            ),
            responseFields(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("에러 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
            )
        );
    }

    public static RestDocumentationResultHandler getMember() {
        return docsTemplate("members/findByEmail",
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

    public static RestDocumentationResultHandler getMemberWithoutAuth() {
        return docsTemplate("members/findByEmailWithoutAuth",
            requestParameters(
                parameterWithName("email").description("사용자의 이메일")
            ),
            responseFields(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("에러 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
            )
        );
    }

    public static RestDocumentationResultHandler updateMember() {
        return docsTemplate("members/update",
            pathParameters(
                parameterWithName("id").description("사용자의 고유 id")
            ),
            requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자의 이름"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("사용자의 비밀번호")
            ),
            requestHeaders(
                headerWithName("Authorization").description("사용자의 JWT 토큰")
            )
        );
    }

    public static RestDocumentationResultHandler deleteMember() {
        return docsTemplate("members/delete",
            pathParameters(
                parameterWithName("id").description("사용자의 고유 id")
            ),
            requestHeaders(
                headerWithName("Authorization").description("The token for login which is Bearer Type")
            )
        );
    }
}
