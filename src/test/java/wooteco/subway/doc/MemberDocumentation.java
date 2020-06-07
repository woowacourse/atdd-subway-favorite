package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

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
		return document("members/read",
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

	public static RestDocumentationResultHandler updateMember() {
		return document("members/update",
			requestFields(
				fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
				fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
			),
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type")
			),
			responseFields(
				fieldWithPath("id").type(JsonFieldType.NUMBER).description("Authorized user's id"),
				fieldWithPath("email").type(JsonFieldType.STRING).description("Authorized user's email address"),
				fieldWithPath("name").type(JsonFieldType.STRING).description("Authorized user's name")
			)
		);
	}

	public static RestDocumentationResultHandler deleteMember() {
		return document("members/delete",
			requestHeaders(
				headerWithName("Authorization").description("The token for login which is Bearer Type")
			)
		);
	}
}
