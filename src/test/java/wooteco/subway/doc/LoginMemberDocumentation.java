package wooteco.subway.doc;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class LoginMemberDocumentation {

    public static RestDocumentationResultHandler login() {
        return document("me/login",
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 시도할 이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 시도할 비밀번호")
                )
        );
    }

    public static ResultHandler getOwnMember() {
        return document("me/get",
                requestHeaders(
                        headerWithName("Authorization").description("본인 정보 조회에 사용할 토큰")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("조회한 본인의 멤버 id"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("조회한 본인의 이메일"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("조회한 본인의 이름")
                )
        );
    }

    public static RestDocumentationResultHandler updateOwnMember() {
        return document("me/update",
                requestHeaders(
                        headerWithName("Authorization").description("본인 정보 수정에 사용할 토큰")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("수정할 본인의 이름"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("수정할 본인의 비밀번호")
                )
        );
    }

    public static ResultHandler deleteOwnMember() {
        return document("me/delete",
                requestHeaders(
                        headerWithName("Authorization").description("본인 정보 삭제에 사용할 토큰")
                )
        );
    }
}
