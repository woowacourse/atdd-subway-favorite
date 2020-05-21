package wooteco.subway.doc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberDocumentation {
	public static RestDocumentationResultHandler createMember() {
		return document("members/create");
	}

	public static RestDocumentationResultHandler updateMember() {
		return document("members/update",
			requestFields(
				fieldWithPath("name").type(JsonFieldType.STRING)
					.description("The user's name"),
				fieldWithPath("password").type(JsonFieldType.STRING)
					.description("The user's password")
			),
			requestHeaders(
				headerWithName("Authorization").description(
					"The token for login which is Bearer Type")
			)
		);
	}
}
