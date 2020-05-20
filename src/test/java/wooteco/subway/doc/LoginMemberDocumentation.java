package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class LoginMemberDocumentation {
	public static RestDocumentationResultHandler login() {
		return document("oauth/token",
			requestFields(
				fieldWithPath("email").type(JsonFieldType.STRING).description("Input email address"),
				fieldWithPath("password").type(JsonFieldType.STRING).description("Input email address")
			),
			responseFields(
				fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Authorized access Token"),
				fieldWithPath("tokenType").type(JsonFieldType.STRING).description("Authorized Token Type")
			)
		);
	}

	public static RestDocumentationResultHandler getAuthorizedMember() {
		return document("oauth/member",
			requestHeaders(
				headerWithName("authorization").description("The user's authorized token")
			),
			responseFields(
				fieldWithPath("id").type(JsonFieldType.NUMBER).description("Authorized user's id"),
				fieldWithPath("email").type(JsonFieldType.STRING).description("Authorized user's email address"),
				fieldWithPath("name").type(JsonFieldType.STRING).description("Authorized user's name")
			)
		);
	}
}
