package wooteco.subway.doc;


import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

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

//    public static RestDocumentationResultHandler updateMember() {
//        return document("members/update",
//                requestFields(
//                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
//                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
//                ),
//                requestHeaders(
//                        headerWithName("Authorization").description("The token for login which is Bearer Type")
//                )
//        );
//    }
}
