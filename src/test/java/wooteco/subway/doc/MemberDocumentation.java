package wooteco.subway.doc;


import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

public class MemberDocumentation {
	public static RestDocumentationResultHandler createMember() {
		return document("members/create",
		                requestFields(
				                fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email " +
						                                                                              "address"),
				                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
				                fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password"),
						fieldWithPath("confirmPassword").type(JsonFieldType.STRING).description("The user's confirm password")
				),
		                responseHeaders(
				                headerWithName("Location").description("The user's location who just created")
		                )
		);
	}

	public static RestDocumentationResultHandler updateMember() {
		return document("members/update",
		                requestFields(
				                fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email").optional(),
				                fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name").optional(),
				                fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password").optional(),
				                fieldWithPath("confirmPassword").type(JsonFieldType.STRING).description("The user's " +
						                                                                                        "confirm password").optional()
		                )
		);
	}
}
