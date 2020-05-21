package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class LoginMemberDocumentation {

    public static RestDocumentationResultHandler loginMember() {
        return document("login/login",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("값 없음").optional(),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("값 없음").optional(),
                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("JWT value"),
                        fieldWithPath("data.tokenType").type(JsonFieldType.STRING).description("Authorization type")
                )
        );
    }

    public static RestDocumentationResultHandler getMyInfo() {
        return document("login/getMyInfo",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NULL).description("값 없음").optional(),
                        fieldWithPath("message").type(JsonFieldType.NULL).description("값 없음").optional(),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("The user's id"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("The user's name"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("The user's email")
                )
        );
    }

    public static RestDocumentationResultHandler deleteMyInfo() {
        return document("login/deleteMyInfo",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }

    public static RestDocumentationResultHandler updataMyInfo() {
        return document("login/updateMyInfo",
                requestHeaders(
                        headerWithName("Authorization").description("The token for login which is Bearer Type")
                )
        );
    }
}
