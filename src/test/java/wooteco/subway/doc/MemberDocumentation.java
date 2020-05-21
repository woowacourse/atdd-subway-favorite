package wooteco.subway.doc;


import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class MemberDocumentation {
	public static RestDocumentationResultHandler createMember() {
		return document("members/create",
				requestFields(
						fieldWithPath("email").type(JsonFieldType.STRING).description("User 이메일"),
						fieldWithPath("name").type(JsonFieldType.STRING).description("User 이름"),
						fieldWithPath("password").type(JsonFieldType.STRING).description("User 비밀번호")
				),
				responseHeaders(
						headerWithName("Location").description("User 정보가 생성된 location")
				)
		);
	}

	public static RestDocumentationResultHandler readMember() {
		return document("members/read",
				requestParameters(
						parameterWithName("email").description("User 이메일"))
		);
	}

	public static RestDocumentationResultHandler updateMember() {
		return document("members/update",
				pathParameters(
						parameterWithName("id").description("수정할 User id")
				),
				requestFields(
						fieldWithPath("name").type(JsonFieldType.STRING).description("User의 새로운 이름"),
						fieldWithPath("password").type(JsonFieldType.STRING).description("User의 새로운 비밀번호")
				)
		);
	}

	public static RestDocumentationResultHandler deleteMember() {
		return document("members/delete",
				pathParameters(
						parameterWithName("id").description("탈퇴할 User id")
				)
		);
	}
}
