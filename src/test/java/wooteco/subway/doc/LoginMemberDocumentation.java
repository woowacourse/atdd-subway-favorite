package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class LoginMemberDocumentation {

    public static RestDocumentationResultHandler login() {
        return document("loginMembers/login",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password")
            ),
            responseFields(
                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                    .description("The user's access token"),
                fieldWithPath("tokenType").type(JsonFieldType.STRING)
                    .description("The user's token type")
            )
        );
    }

    public static RestDocumentationResultHandler getMember() {
        return document("loginMembers/getMember",
            // TODO request에 attribute가 어디 있는지 확인
            // requestFields(
            //     fieldWithPath("loginMemberEmail").description("The user's email address")
            // ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
            )
        );
    }

    public static RestDocumentationResultHandler editMember() {
        return document("loginMembers/editMember",
            // TODO
            requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
            )
        );
    }

    public static RestDocumentationResultHandler deleteMember() {
        return document("loginMembers/deleteMember",
            // TODO
            requestHeaders(
                headerWithName("Authorization").description(
                    "The token for login which is Bearer Type")
            )
        );
    }
}
