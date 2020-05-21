package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberDocumentation {
	public static RestDocumentationResultHandler createMember() {
		return document("members/create",
			responseHeaders(
				headerWithName("Location").description("The user's location who just created")
			),
			requestFields(
				fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
				fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
				fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
			)
		);
	}

	public static RestDocumentationResultHandler login() {
		return document("member/login",
			requestFields(
				fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email"),
				fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
			),
			responseFields(
				fieldWithPath("accessToken").type(JsonFieldType.STRING).description("The user's accessToken"),
				fieldWithPath("tokenType").type(JsonFieldType.STRING).description("The user's tokenType")
			)
		);
	}

	public static RestDocumentationResultHandler getMember() {
		return document("members/get",
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type")
			),
			responseFields(
				fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's persist id"),
				fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
				fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"))
		);
	}

	public static RestDocumentationResultHandler updateMember() {
		return document("members/update",
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type")
			),
			requestFields(
				fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
				fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
			)
		);
	}

	public static RestDocumentationResultHandler deleteMember() {
		return document("member/delete",
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type")
			)
		);
	}
}
