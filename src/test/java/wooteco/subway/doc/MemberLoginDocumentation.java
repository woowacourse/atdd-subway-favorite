package wooteco.subway.doc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

/**
 *    class description
 *
 *    @author HyungJu An
 */
public class MemberLoginDocumentation {
	public static RestDocumentationResultHandler createToken() {
		return document("oauth/token",
			requestFields(
				fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
				fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
			),
			responseFields(
				fieldWithPath("accessToken").type(JsonFieldType.STRING).description("The user's access token"),
				fieldWithPath("tokenType").type(JsonFieldType.STRING).description("The token's type")
			)
		);
	}
}
